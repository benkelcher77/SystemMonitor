package ui.gpu

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ui.LineChart
import ui.LineChartData

@Composable
fun GPUStatus(
    vm: GPUStatusViewModel
) {
    var gpuTempChart by remember {
        mutableStateOf(LineChartData(
            points = emptyList(),
            xRange = 0 to 100,
            yRange = 20 to 80,
            xTicks = emptyList(),
            yTicks = (2..8).map {
                it * 10 to (it * 10).toString()
            },
        ))
    }

    var gpuFanChart by remember {
        mutableStateOf(LineChartData(
            points = emptyList(),
            xRange = 0 to 100,
            yRange = 0 to 100,
            xTicks = emptyList(),
            yTicks = (0..10).map {
                it * 10 to (it * 10).toString()
            },
        ))
    }

    var displayedHistorySize by remember { mutableStateOf(100) }
    LaunchedEffect(displayedHistorySize) {
        gpuTempChart = gpuTempChart.copy(xRange = 0 to displayedHistorySize)
        gpuFanChart = gpuFanChart.copy(xRange = 0 to displayedHistorySize)
    }

    LaunchedEffect(Unit) {
        vm.gpuTempHistoryFlow.collect { history ->
            gpuTempChart = gpuTempChart.copy(
                points = history
                    .dropLast((history.size - displayedHistorySize).coerceAtLeast(0))
                    .mapIndexed { index, temp ->
                        Pair(displayedHistorySize - 1 - index, temp)
                    }
            )
        }
    }

    LaunchedEffect(Unit) {
        vm.gpuFanHistoryFlow.collect { history ->
            gpuFanChart = gpuFanChart.copy(
                points = history
                    .dropLast((history.size - displayedHistorySize).coerceAtLeast(0))
                    .mapIndexed { index, fanPercentage ->
                        Pair(displayedHistorySize - 1 - index, fanPercentage)
                    }
            )
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Button(onClick = { displayedHistorySize = (displayedHistorySize - 1).coerceAtLeast(8) }) { Text("<") }
            Text("Displayed Entries: $displayedHistorySize")
            Button(onClick = { displayedHistorySize = (displayedHistorySize + 1).coerceAtMost(GPUStatusViewModel.HISTORY_BUFFER_COUNT) }) { Text(">") }
        }
        LineChart(modifier = Modifier.fillMaxWidth().weight(1f).padding(4.dp), data = gpuTempChart)
        LineChart(modifier = Modifier.fillMaxWidth().weight(1f).padding(4.dp), data = gpuFanChart)
    }
}