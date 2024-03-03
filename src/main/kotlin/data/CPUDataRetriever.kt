package data

import util.ShellCommandUtils.runAsProcess

interface ICPUDataRetriever {

    fun getCPUTemp(): Int
    fun getCPUFanRPM(): Int
    fun getCPUUsage(): String

}

class CPUDataRetriever : ICPUDataRetriever {
    override fun getCPUTemp(): Int {
        return 30
    }

    override fun getCPUFanRPM(): Int {
        return 1000
    }

    override fun getCPUUsage(): String {
        return "cat /proc/stat".runAsProcess() ?: "null"
    }

}