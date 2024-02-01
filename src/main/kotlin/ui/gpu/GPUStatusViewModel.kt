package ui.gpu

import data.IGPUDataRetriever
import jni.NVMLBridge
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GPUStatusViewModel(
    private val dataRetriever: IGPUDataRetriever
) {

    val gpuTempFlow: Flow<Float> = flow {
        while (true) {
            val gpuTemp = dataRetriever.getTemp()
            emit(gpuTemp)
            delay(REFRESH_DELAY)
        }
    }

    val gpuFanFlow: Flow<Int> = flow {
        while (true) {
            val fanPercentage = dataRetriever.getFanSpeedPercent()
            emit(fanPercentage)
            delay(REFRESH_DELAY)
        }
    }

    fun init() {
        NVMLBridge.nvmlInit()
    }

    fun shutdown() {
        NVMLBridge.nvmlShutdown()
    }

    companion object {
        private const val REFRESH_DELAY = 2000L
    }

}