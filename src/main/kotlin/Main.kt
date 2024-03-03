
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import data.CPUDataRetriever
import data.GPUDataRetriever
import ui.ViewMode
import ui.ViewModeSelector
import ui.cpu.CPUStatus
import ui.cpu.CPUStatusViewModel
import ui.gpu.GPUStatus
import ui.gpu.GPUStatusViewModel

@Composable
fun SystemMonitor(
    cpuViewModel: CPUStatusViewModel,
    gpuViewModel: GPUStatusViewModel,
) {
    var viewMode by remember { mutableStateOf(ViewMode.CPU) }
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        ViewModeSelector(viewMode) {
            viewMode = it
            when (it) {
                ViewMode.CPU -> {
                    cpuViewModel.init()
                    gpuViewModel.shutdown()
                }
                ViewMode.GPU -> {
                    gpuViewModel.init()
                    cpuViewModel.shutdown()
                }
            }
        }
        when (viewMode) {
            ViewMode.CPU -> CPUStatus(cpuViewModel)
            ViewMode.GPU -> GPUStatus(gpuViewModel)
        }
    }
}

fun main() = application {
    val windowState = rememberWindowState(width = 1280.dp, height = 720.dp)

    val cpuViewModel = CPUStatusViewModel(CPUDataRetriever())
    val gpuViewModel = GPUStatusViewModel(GPUDataRetriever())

    cpuViewModel.init()

    Window(
        title = "System Monitor 1.0.0",
        state = windowState,
        onCloseRequest = ::exitApplication
    ) {
        MaterialTheme {
            SystemMonitor(cpuViewModel, gpuViewModel)
        }
    }
}
