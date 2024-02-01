package data

import jni.NVMLBridge

interface IGPUDataRetriever {

    fun getTemp(): Float
    fun getFanSpeedPercent(): Int

}

class GPUDataRetriever : IGPUDataRetriever {
    override fun getTemp(): Float {
        return 30f * (Math.random().toFloat()) // Dummy temperature data (for now!)
    }

    override fun getFanSpeedPercent(): Int {
        return NVMLBridge.nvmlDeviceGetFanSpeed()
    }

}