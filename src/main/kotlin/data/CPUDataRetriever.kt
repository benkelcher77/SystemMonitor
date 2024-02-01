package data

interface ICPUDataRetriever {

    fun getCPUTemp(): Float
    fun getCPUFanRPM(): Int

}

class CPUDataRetriever : ICPUDataRetriever {
    override fun getCPUTemp(): Float {
        return 30f
    }

    override fun getCPUFanRPM(): Int {
        return 1000
    }

}