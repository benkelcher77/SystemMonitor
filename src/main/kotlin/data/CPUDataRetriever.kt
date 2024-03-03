package data

import util.ShellCommandUtils.runAsProcess

interface ICPUDataRetriever {

    fun getCPUTemp(): Int
    fun getCPUFanRPM(): Int
    fun getCPUUsage(): Float

}

class CPUDataRetriever : ICPUDataRetriever {
    override fun getCPUTemp(): Int {
        return 30
    }

    override fun getCPUFanRPM(): Int {
        return 1000
    }

    override fun getCPUUsage(): Float =
        "cat /proc/stat".runAsProcess()
            ?.trim()
            ?.split("\n")?.firstOrNull() // Overall CPU usage is on the first line of /proc/stat
            ?.split("\\s+".toRegex())
            ?.drop(1) // Drop the "cpu" label
            ?.map { it.toFloatOrNull() ?: 1f }
            ?.let { values ->
                (1f - values[CPU_USAGE_IDLE_INDEX] / values.sum()) * 100f
            } ?: -1f

    companion object {
        const val CPU_USAGE_IDLE_INDEX = 3
    }
}