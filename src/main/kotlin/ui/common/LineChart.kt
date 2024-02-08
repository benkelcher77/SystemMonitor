package ui.common

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer

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

internal fun remap(x: Number, oldMin: Number, oldMax: Number, newMin: Number, newMax: Number): Float =
    (x.toFloat() - oldMin.toFloat()) / (oldMax.toFloat() - oldMin.toFloat()) * (newMax.toFloat() - newMin.toFloat()) + newMin.toFloat()

@Composable
fun <TypeX : Number, TypeY: Number> LineChart(
    modifier: Modifier = Modifier,
    data: LineChartData<TypeX, TypeY>? = null,
) {
    var layout: LayoutCoordinates? = null
    val textMeasurer = rememberTextMeasurer()
    Canvas(
        modifier = modifier
            .onGloballyPositioned { layout = it }
    ) {
        // Background. TODO customizable
        drawRect(color = Color.LightGray, size = this.size)

        // Axes
        val tl = layout?.let { Offset(it.size.width.toFloat() * 0.1f, it.size.height.toFloat() * 0.1f) }
            ?: Offset(0f, 0f)
        val tr = layout?.let { Offset(it.size.width.toFloat() * 0.9f, it.size.height.toFloat() * 0.1f) }
            ?: Offset(0f, 0f)
        val bl = layout?.let { Offset(it.size.width.toFloat() * 0.1f, it.size.height.toFloat() * 0.9f) }
            ?: Offset(0f, 0f)
        val br = layout?.let { Offset(it.size.width.toFloat() * 0.9f, it.size.height.toFloat() * 0.9f) }
            ?: Offset(0f, 0f)

        drawLine(color = Color.Black, start = bl, end = br, strokeWidth = 4f)
        drawLine(color = Color.Black, start = bl, end = tl, strokeWidth = 4f)

        data?.takeIf { it.points.isNotEmpty() }?.let {
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

                        drawLine(color = Color.DarkGray, start = start, end = end, strokeWidth = 2f)
                        prevPoint = point
                    }
                }
            } else {
                // TODO Handle single point
            }

            // Draw the ticks
            it.yTicks.forEach { (y, str) ->
                drawText(
                    textMeasurer,
                    text = str,
                    topLeft = Offset(bl.x - 32f, remap(y, it.yMin(), it.yMax(), bl.y, tl.y)),
                )
                drawLine(
                    color = Color.Black,
                    start = Offset(bl.x - 16f, remap(y, it.yMin(), it.yMax(), bl.y, tl.y)),
                    end = Offset(bl.x, remap(y, it.yMin(), it.yMax(), bl.y, tl.y)),
                    strokeWidth = 2f,
                )
            }

            it.xTicks.forEach { (x, str) ->
                drawText(
                    textMeasurer,
                    text = str,
                    topLeft = Offset(remap(x, it.xMin(), it.xMax(), bl.x, br.x), bl.y + 32f)
                )
                drawLine(
                    color = Color.Black,
                    start = Offset(remap(x, it.xMin(), it.xMax(), bl.x, br.x), bl.y + 16f),
                    end = Offset(remap(x, it.xMin(), it.xMax(), bl.x, br.x), bl.y),
                    strokeWidth = 2f
                )
            }
        }
    }
}