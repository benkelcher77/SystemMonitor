package ui.cpu

import data.ICPUDataRetriever
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CPUStatusViewModel(
    dataRetriever: ICPUDataRetriever,
) {

    @Volatile
    private var running: Boolean = false

    val cpuUsageFlow: Flow<String> = flow {
        while (true) {
            if (running) {
                val usage = dataRetriever.getCPUUsage()
                emit(usage)
                delay(REFRESH_DELAY)
            }
        }
    }

    fun init() {
        running = true
    }

    fun shutdown() {
        running = false
    }

    companion object {
        private const val REFRESH_DELAY = 1000L
    }

}