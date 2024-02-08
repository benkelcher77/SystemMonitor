package data

import jni.NVMLBridge

data class MemoryInfo(
    val free: Long,
    val total: Long,
    val used: Long,
)

interface IGPUDataRetriever {

    fun getTemp(): Int
    fun getFanSpeedPercent(): Int
    fun getMemoryInfo(): MemoryInfo

}

class GPUDataRetriever : IGPUDataRetriever {

    override fun getTemp(): Int {
        return NVMLBridge.nvmlDeviceGetTemperature()
    }

    override fun getFanSpeedPercent(): Int {
        return NVMLBridge.nvmlDeviceGetFanSpeed()
    }

    override fun getMemoryInfo(): MemoryInfo {
        val memoryInfoArray = NVMLBridge.nvmlDeviceGetMemoryInfo()
        return MemoryInfo(memoryInfoArray[0], memoryInfoArray[1], memoryInfoArray[2])
    }

}