package ui.cpu

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@Composable
fun CPUStatus(viewModel: CPUStatusViewModel) {
    val usage by viewModel.cpuUsageFlow.collectAsState("NOT INITIALIZED")
    Text(usage)
}