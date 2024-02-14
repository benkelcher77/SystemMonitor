package ui.gpu

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import data.MemoryInfo
import ui.common.Collapsible
import ui.common.LineChart
import ui.common.LineChartConfig
import ui.common.LineChartData
import util.SizeUnitUtils

internal const val MIN_ENTRIES_SHOWN = 32
internal const val ENTRIES_STEP_SIZE = 32

@Composable
fun GPUStatus(
    vm: GPUStatusViewModel
) {
    var gpuTempChart by remember {
        mutableStateOf(
            LineChartData(
            points = emptyList(),
            xRange = 0 to 100,
            yRange = 20 to 80,
            xTicks = emptyList(),
            yTicks = (2..8).map {
                it * 10 to (it * 10).toString()
            },
        )
        )
    }

    var gpuFanChart by remember {
        mutableStateOf(
            LineChartData(
            points = emptyList(),
            xRange = 0 to 100,
            yRange = 0 to 100,
            xTicks = emptyList(),
            yTicks = (0..10).map {
                it * 10 to (it * 10).toString()
            },
        )
        )
    }

    var gpuMemoryChart by remember {
        mutableStateOf(LineChartData(
            points = emptyList(),
            xRange = 0 to 100,
            yRange = 0.0 to 100.0,
            xTicks = emptyList(),
            yTicks = (0..10).map {
                (it * 10).toFloat() to (it * 10).toString()
            }
        ))
    }

    var displayedHistorySize by remember { mutableStateOf(128) }
    LaunchedEffect(displayedHistorySize) {
        gpuTempChart = gpuTempChart.copy(
            xRange = 0 to displayedHistorySize,
            xTicks = (0..10).map {
                it * displayedHistorySize / 10 to ""
            }
        )
        gpuFanChart = gpuFanChart.copy(xRange = 0 to displayedHistorySize)
        gpuMemoryChart = gpuMemoryChart.copy(xRange = 0 to displayedHistorySize)
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

    LaunchedEffect(Unit) {
        vm.gpuMemoryInfoHistoryFlow.collect { history ->
            gpuMemoryChart = gpuMemoryChart.copy(
                points = history
                    .dropLast((history.size - displayedHistorySize).coerceAtLeast(0))
                    .mapIndexed { index, memory ->
                        Pair(displayedHistorySize - 1 - index, (memory.used / memory.total).toDouble())
                    }
            )
        }
    }

    val currentTemp by vm.gpuTempFlow.collectAsState(0)
    val currentFanSpeed by vm.gpuFanFlow.collectAsState(0)
    val currentGPUMemoryInfo by vm.gpuMemoryInfoFlow.collectAsState(MemoryInfo(1, 1, 0))

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            var sliderValue by remember { mutableStateOf(displayedHistorySize.toFloat()) }

            Text(text = "${sliderValue.toInt()} Entries")

            // TODO Make a wrapper over this that uses Int rather than Float.
            Slider(
                value = sliderValue,
                valueRange = MIN_ENTRIES_SHOWN.toFloat()..GPUStatusViewModel.HISTORY_BUFFER_COUNT.toFloat(),
                onValueChange = { value -> sliderValue = value },
                onValueChangeFinished = { displayedHistorySize = sliderValue.toInt() },
                colors = SliderDefaults.colors(
                    thumbColor = Color.Red,
                    activeTrackColor = Color.Red,
                    inactiveTrackColor = Color.DarkGray,
                ),
            )
        }

        @Composable
        fun CollapsedContent(text: String, open: Boolean) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.LightGray, shape = RoundedCornerShape(4.dp))
                    .border(width = 2.dp, color = Color.DarkGray)
                    .padding(4.dp),
                text = "${if (open) "v" else ">"} | $text"
            )
        }

        Collapsible(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            weight = 1f,
            collapsedContent = { open ->
                CollapsedContent("GPU Temperature (${currentTemp}°C)", open)
            }
        ) {
            LineChart(
                modifier = Modifier.fillMaxSize(),
                data = gpuTempChart,
                config = LineChartConfig(
                    lineThickness = 2f,
                    lineColor = Color.Red,
                    tickThickness = 1f,
                    extendTicks = true
                )
            )
        }

        Collapsible(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            weight = 1f,
            collapsedContent = { open ->
                CollapsedContent("GPU Fan Speed (${currentFanSpeed}%)", open)
            }
        ) { LineChart(modifier = Modifier.fillMaxSize(), data = gpuFanChart) }

        val gpuMemoryUsageString = "${SizeUnitUtils.bytesToHumanReadable(currentGPUMemoryInfo.used.toDouble()).displayString()} / ${SizeUnitUtils.bytesToHumanReadable(currentGPUMemoryInfo.total.toDouble()).displayString()}"
        Collapsible(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            weight = 1f,
            collapsedContent = { open ->
                CollapsedContent("GPU Memory Usage ($gpuMemoryUsageString)", open)
            }
        ) { LineChart(modifier = Modifier.fillMaxSize(), data = gpuMemoryChart) }
    }
}