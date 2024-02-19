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

    @Volatile
    var nvidiaDriverVersion: String = NVIDIA_DRIVER_VERSION_DEFAULT_VALUE
        private set

    @Volatile
    var cudaDriverVersion: String = CUDA_DRIVER_VERSION_DEFAULT_VALUE
        private set

    @Volatile
    var nvmlVersion: String = NVML_VERSION_DEFAULT_VALUE
        private set

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
        if (NVMLBridge.nvmlInit()) {
            nvmlInitialized = true
            nvidiaDriverVersion = NVMLBridge.nvmlSystemGetDriverVersion()
            cudaDriverVersion = NVMLBridge.nvmlSystemGetCudaDriverVersion().toString()
            nvmlVersion = NVMLBridge.nvmlSystemGetNVMLVersion()
        }
    }

    fun shutdown() {
        if (NVMLBridge.nvmlShutdown()) {
            nvmlInitialized = false
            nvidiaDriverVersion = NVIDIA_DRIVER_VERSION_DEFAULT_VALUE
            cudaDriverVersion = CUDA_DRIVER_VERSION_DEFAULT_VALUE
            nvmlVersion = NVML_VERSION_DEFAULT_VALUE
        }
    }

    companion object {
        private const val REFRESH_DELAY = 1000L
        const val HISTORY_BUFFER_COUNT = 1024

        private const val NVIDIA_DRIVER_VERSION_DEFAULT_VALUE = "Unknown"
        private const val CUDA_DRIVER_VERSION_DEFAULT_VALUE = "Unknown"
        private const val NVML_VERSION_DEFAULT_VALUE = "Unknown"
    }

}