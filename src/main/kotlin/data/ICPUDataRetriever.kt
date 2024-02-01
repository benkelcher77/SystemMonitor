package data

interface ICPUDataRetriever {

    fun getCPUTemp(): Float
    fun getCPUFanRPM(): Int

}