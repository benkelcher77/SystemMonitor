import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState

@Composable
fun SystemMonitor() {
    var text by remember { mutableStateOf("Hello, World!") }

    Button(onClick = {
        text = "Hello, Desktop!"
    }) {
        Text(text)
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
