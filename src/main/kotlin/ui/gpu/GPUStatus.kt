package ui.gpu

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
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

    LaunchedEffect(Unit) {
        vm.gpuTempFlow.collect {
            val shiftedPoints = gpuTempChart.points
                .map { point -> Pair(point.first - 1, point.second) }
                .filter { point -> point.first >= 0 }
                .toMutableList()

            shiftedPoints.add(gpuTempChart.xMax() to it)
            gpuTempChart = gpuTempChart.copy(points = shiftedPoints)
        }
    }

    LaunchedEffect(Unit) {
        vm.gpuFanFlow.collect {
            val shiftedPoints = gpuFanChart.points
                .map { point -> Pair(point.first - 1, point.second) }
                .filter { point -> point.first >= 0 }
                .toMutableList()

            shiftedPoints.add(gpuFanChart.xMax() to it)
            gpuFanChart = gpuFanChart.copy(points = shiftedPoints)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        LineChart(modifier = Modifier.fillMaxWidth().weight(1f).padding(4.dp), data = gpuTempChart)
        LineChart(modifier = Modifier.fillMaxWidth().weight(1f).padding(4.dp), data = gpuFanChart)
    }
}