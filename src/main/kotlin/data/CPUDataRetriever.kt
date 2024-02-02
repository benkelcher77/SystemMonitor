package data

interface ICPUDataRetriever {

    fun getCPUTemp(): Int
    fun getCPUFanRPM(): Int

}

class CPUDataRetriever : ICPUDataRetriever {
    override fun getCPUTemp(): Int {
        return 30
    }

    override fun getCPUFanRPM(): Int {
        return 1000
    }

}