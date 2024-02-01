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
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import jni.NVMLBridge
import ui.ViewMode
import ui.ViewModeSelector

@Composable
fun SystemMonitor() {
    var viewMode by remember { mutableStateOf(ViewMode.CPU) }
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        ViewModeSelector(viewMode) { viewMode = it }
        when (viewMode) {
            ViewMode.CPU -> {
                Text(NVMLBridge.getNVMLVersion())
            }
            ViewMode.GPU -> Unit
        }
    }
}

fun main() = application {
    val windowState = rememberWindowState()
    Window(
        title = "System Monitor 1.0.0",
        state = windowState,
        onCloseRequest = ::exitApplication
    ) {
        MaterialTheme {
            SystemMonitor()
        }
    }
}
