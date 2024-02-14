package ui.common

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.min

data class LineChartData<TypeX : Number, TypeY: Number>(
    val points: List<Pair<TypeX, TypeY>>,
    val xRange: Pair<TypeX, TypeX>,
    val yRange: Pair<TypeY, TypeY>,

    val xTicks: List<Pair<TypeX, String>>,
    val yTicks: List<Pair<TypeY, String>>,
) {
    fun xMin(): TypeX = xRange.first
    fun xMax(): TypeX = xRange.second
    fun yMin(): TypeY = yRange.first
    fun yMax(): TypeY = yRange.second
}

data class LineChartConfig(
    val backgroundColor: Color = Color.LightGray,
    val lineColor: Color = Color.DarkGray,
    val lineThickness: Float = 1f,
    val axisColor: Color = Color.DarkGray,
    val axisThickness: Float = 2f,
    val tickColor: Color = Color.DarkGray,
    val tickThickness: Float = 1f,
    val extendTicks: Boolean = false,
) {
    companion object {
        val DEFAULT = LineChartConfig()
    }
}

internal fun remap(x: Number, oldMin: Number, oldMax: Number, newMin: Number, newMax: Number): Float =
    (x.toFloat() - oldMin.toFloat()) / (oldMax.toFloat() - oldMin.toFloat()) * (newMax.toFloat() - newMin.toFloat()) + newMin.toFloat()

@Composable
fun <TypeX : Number, TypeY: Number> LineChart(
    modifier: Modifier = Modifier,
    data: LineChartData<TypeX, TypeY>? = null,
    config: LineChartConfig = LineChartConfig.DEFAULT,
) {
    var layout: LayoutCoordinates? = null
    val textMeasurer = rememberTextMeasurer()
    Canvas(
        modifier = modifier
            .onGloballyPositioned { layout = it }
    ) {
        drawRect(color = config.backgroundColor, size = this.size)

        // Axes
        val tl = layout?.let { Offset(it.size.width.toFloat() * 0.1f, it.size.height.toFloat() * 0.1f) }
            ?: Offset(0f, 0f)
        val tr = layout?.let { Offset(it.size.width.toFloat() * 0.9f, it.size.height.toFloat() * 0.1f) }
            ?: Offset(0f, 0f)
        val bl = layout?.let { Offset(it.size.width.toFloat() * 0.1f, it.size.height.toFloat() * 0.9f) }
            ?: Offset(0f, 0f)
        val br = layout?.let { Offset(it.size.width.toFloat() * 0.9f, it.size.height.toFloat() * 0.9f) }
            ?: Offset(0f, 0f)

        drawLine(color = config.axisColor, start = bl, end = br, strokeWidth = config.axisThickness)
        drawLine(color = config.axisColor, start = bl, end = tl, strokeWidth = config.axisThickness)

        data?.takeIf { it.points.isNotEmpty() }?.let {
            // Draw the ticks
            val tickSize = min(size.width, size.height) * 0.025f
            val tickXEnd = if (config.extendTicks) br.x else bl.x
            it.yTicks.forEach { (y, str) ->
                drawLine(
                    color = config.tickColor,
                    start = Offset(bl.x - tickSize, remap(y, it.yMin(), it.yMax(), bl.y, tl.y)),
                    end = Offset(tickXEnd, remap(y, it.yMin(), it.yMax(), bl.y, tl.y)),
                    strokeWidth = config.tickThickness,
                )

                val textMeasurements = textMeasurer.measure(str)
                drawText(
                    textMeasurer,
                    text = str,
                    topLeft = Offset(
                        bl.x - tickSize - textMeasurements.size.width,
                        remap(y, it.yMin(), it.yMax(), bl.y, tl.y) - 0.5f * textMeasurements.size.height
                    ),
                )
            }

            val tickYEnd = if (config.extendTicks) tl.y else bl.y
            it.xTicks.forEach { (x, str) ->
                drawLine(
                    color = config.tickColor,
                    start = Offset(remap(x, it.xMin(), it.xMax(), bl.x, br.x), bl.y + tickSize),
                    end = Offset(remap(x, it.xMin(), it.xMax(), bl.x, br.x), tickYEnd),
                    strokeWidth = config.tickThickness,
                )

                val textMeasurements = textMeasurer.measure(str)
                drawText(
                    textMeasurer,
                    text = str,
                    topLeft = Offset(
                        remap(x, it.xMin(), it.xMax(), bl.x, br.x) - 0.5f * textMeasurements.size.width,
                        bl.y + tickSize
                    ),
                )
            }

            // Draw the data
            if (it.points.size > 1) {
                var prevPoint = it.points[0]
                it.points.forEachIndexed { index, point ->
                    if (index != 0) {
                        val start = Offset(
                            remap(prevPoint.first, it.xMin(), it.xMax(), tl.x, tr.x),
                            remap(prevPoint.second, it.yMin(), it.yMax(), bl.y, tl.y)
                        )

                        val end = Offset(
                            remap(point.first, it.xMin(), it.xMax(), tl.x, tr.x),
                            remap(point.second, it.yMin(), it.yMax(), bl.y, tl.y)
                        )

                        drawLine(color = config.lineColor, start = start, end = end, strokeWidth = config.lineThickness)
                        prevPoint = point
                    }
                }
            } else {
                // TODO Handle single point
            }
        }
    }
}