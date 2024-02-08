package jni

object NVMLBridge {


    init {
        System.loadLibrary("nvml-bindings")
    }

    external fun nvmlInit(): Boolean
    external fun nvmlShutdown(): Boolean

    external fun nvmlSystemGetCudaDriverVersion(): Int
    external fun nvmlSystemGetDriverVersion(): String
    external fun nvmlSystemGetNVMLVersion(): String

    external fun nvmlDeviceGetMemoryInfo(): LongArray

    external fun nvmlDeviceGetFanSpeed(): Int
    external fun nvmlDeviceGetTemperature(): Int

}