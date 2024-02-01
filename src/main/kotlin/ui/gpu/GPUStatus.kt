package ui.gpu

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState

@Composable
fun GPUStatus(
    vm: GPUStatusViewModel
) {
    val gpuTemp = vm.gpuTempFlow.collectAsState(0f)
    val gpuFanSpeed = vm.gpuFanFlow.collectAsState(5)
    Column {
        Text("Temperature is ${gpuTemp.value}")
        Text("Fan Percentage is ${gpuFanSpeed.value}%")
    }
}