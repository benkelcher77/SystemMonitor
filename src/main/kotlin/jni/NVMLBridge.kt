package jni

object NVMLBridge {

    init {
        System.loadLibrary("nvml-bindings")
    }

    external fun getNVMLVersion(): String

}