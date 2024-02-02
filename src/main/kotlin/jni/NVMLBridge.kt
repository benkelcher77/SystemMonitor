package jni

object NVMLBridge {

    init {
        System.loadLibrary("nvml-bindings")
    }

    external fun nvmlInit(): Boolean
    external fun nvmlShutdown(): Boolean

    external fun nvmlDeviceGetFanSpeed(): Int
    external fun nvmlDeviceGetTemperature(): Int

}