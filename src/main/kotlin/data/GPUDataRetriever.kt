package data

import jni.NVMLBridge

interface IGPUDataRetriever {

    fun getTemp(): Int
    fun getFanSpeedPercent(): Int

}

class GPUDataRetriever : IGPUDataRetriever {
    override fun getTemp(): Int {
        return NVMLBridge.nvmlDeviceGetTemperature()
    }

    override fun getFanSpeedPercent(): Int {
        return NVMLBridge.nvmlDeviceGetFanSpeed()
    }

}