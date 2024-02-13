import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import data.GPUDataRetriever
import jni.NVMLBridge
import ui.ViewMode
import ui.ViewModeSelector
import ui.gpu.GPUStatus
import ui.gpu.GPUStatusViewModel

@Composable
fun SystemMonitor(gpuViewModel: GPUStatusViewModel) {
    var viewMode by remember { mutableStateOf(ViewMode.CPU) }
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        ViewModeSelector(viewMode) {
            viewMode = it
            when (it) {
                ViewMode.CPU -> {
                    gpuViewModel.shutdown()
                }
                ViewMode.GPU -> {
                    gpuViewModel.init()
                }
            }
        }
        when (viewMode) {
            ViewMode.CPU -> {
                Text("Unimplemented")
            }
            ViewMode.GPU -> GPUStatus(gpuViewModel)
        }
    }
}

fun main() = application {
    val windowState = rememberWindowState(width = 1280.dp, height = 720.dp)

    val gpuViewModel = GPUStatusViewModel(GPUDataRetriever())

    Window(
        title = "System Monitor 1.0.0",
        state = windowState,
        onCloseRequest = ::exitApplication
    ) {
        MaterialTheme {
            SystemMonitor(gpuViewModel)
        }
    }
}
