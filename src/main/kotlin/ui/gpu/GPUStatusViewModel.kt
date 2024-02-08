package ui.gpu

import data.IGPUDataRetriever
import data.MemoryInfo
import jni.NVMLBridge
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import util.FlowUtils.accumulate

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

    val gpuTempHistoryFlow = gpuTempFlow
        .accumulate(HISTORY_BUFFER_COUNT)
        .transform { emit(it.reversed()) }

    val gpuFanFlow: Flow<Int> = flow {
        while (true) {
            if (nvmlInitialized) {
                val fanPercentage = dataRetriever.getFanSpeedPercent()
                emit(fanPercentage)
            }
            delay(REFRESH_DELAY)
        }
    }

    val gpuFanHistoryFlow = gpuFanFlow
        .accumulate(HISTORY_BUFFER_COUNT)
        .transform { emit(it.reversed()) }

    val gpuMemoryInfoFlow: Flow<MemoryInfo> = flow {
        while (true) {
            if (nvmlInitialized) {
                val info = dataRetriever.getMemoryInfo()
                emit(info)
            }
            delay(REFRESH_DELAY)
        }
    }

    val gpuMemoryInfoHistoryFlow = gpuMemoryInfoFlow
        .accumulate(HISTORY_BUFFER_COUNT)
        .transform { emit(it.reversed()) }

    fun init() {
        if (NVMLBridge.nvmlInit()) nvmlInitialized = true
    }

    fun shutdown() {
        if (NVMLBridge.nvmlShutdown()) nvmlInitialized = false
    }

    companion object {
        private const val REFRESH_DELAY = 200L
        const val HISTORY_BUFFER_COUNT = 1024
    }

}