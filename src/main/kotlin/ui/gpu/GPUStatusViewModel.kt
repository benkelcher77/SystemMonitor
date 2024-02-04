package ui.gpu

import data.IGPUDataRetriever
import jni.NVMLBridge
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GPUStatusViewModel(
    private val dataRetriever: IGPUDataRetriever
) {

    @Volatile
    private var nvmlInitialized = false

    val gpuTempFlow: Flow<Int> = flow {
        while (true) {
            if (nvmlInitialized) {
                val gpuTemp = dataRetriever.getTemp()
                emit(gpuTemp)
            }
            delay(REFRESH_DELAY)
        }
    }

    val gpuFanFlow: Flow<Int> = flow {
        while (true) {
            if (nvmlInitialized) {
                val fanPercentage = dataRetriever.getFanSpeedPercent()
                emit(fanPercentage)
            }
            delay(REFRESH_DELAY)
        }
    }

    fun init() {
        if (NVMLBridge.nvmlInit()) nvmlInitialized = true
    }

    fun shutdown() {
        if (NVMLBridge.nvmlShutdown()) nvmlInitialized = false
    }

    companion object {
        private const val REFRESH_DELAY = 200L
    }

}