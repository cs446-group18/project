package com.cs446group18.delaywise.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.axis.axisLabelComponent
import com.patrykandpatrick.vico.compose.axis.horizontal.bottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.startAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.column.columnChart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.component.shape.dashedShape
import com.patrykandpatrick.vico.compose.component.shape.roundedCornerShape
import com.patrykandpatrick.vico.compose.component.shape.shader.fromBrush
import com.patrykandpatrick.vico.compose.component.shapeComponent
import com.patrykandpatrick.vico.compose.component.textComponent
import com.patrykandpatrick.vico.compose.dimensions.dimensionsOf
import com.patrykandpatrick.vico.compose.style.ChartStyle
import com.patrykandpatrick.vico.compose.style.ProvideChartStyle
import com.patrykandpatrick.vico.compose.style.currentChartStyle
import com.patrykandpatrick.vico.core.DefaultAlpha
import com.patrykandpatrick.vico.core.DefaultColors
import com.patrykandpatrick.vico.core.DefaultDimens
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.axis.horizontal.HorizontalAxis
import com.patrykandpatrick.vico.core.axis.vertical.VerticalAxis
import com.patrykandpatrick.vico.core.chart.DefaultPointConnector
import com.patrykandpatrick.vico.core.chart.composed.plus
import com.patrykandpatrick.vico.core.chart.copy
import com.patrykandpatrick.vico.core.chart.decoration.ThresholdLine
import com.patrykandpatrick.vico.core.chart.line.LineChart
import com.patrykandpatrick.vico.core.component.shape.LineComponent
import com.patrykandpatrick.vico.core.component.shape.Shapes
import com.patrykandpatrick.vico.core.component.shape.shader.DynamicShaders
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.composed.ComposedChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.composed.plus
import com.patrykandpatrick.vico.core.entry.entriesOf
import com.patrykandpatrick.vico.core.entry.entryModelOf
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import kotlin.math.roundToInt

@Composable
fun FlightDelayGraph(keys: List<String>, values: List<Int>) {
    val marker = rememberMarker()
     val thresholdline = rememberThresholdLine(values.average().toFloat())
    var chartEntryModel = entryModelOf(*values.toTypedArray())
    val bottomAxisValueFormatter =
        AxisValueFormatter<AxisPosition.Horizontal.Bottom> { x, _ -> keys[x.toInt()] }
    ProvideChartStyle(m3ChartStyle()) {
        Chart(
            marker = marker,
            chart = columnChart(decorations = remember(thresholdline) { listOf(thresholdline)}),
            model = chartEntryModel,
            startAxis = startAxis(
                horizontalLabelPosition = VerticalAxis.HorizontalLabelPosition.Inside,
                label = rememberStartAxisLabel(),
                guideline = null,
                maxLabelCount = 3
            ),
            bottomAxis = bottomAxis(
                guideline = null,
                tickPosition = HorizontalAxis.TickPosition.Center(0, 1),
                valueFormatter = bottomAxisValueFormatter,
                labelRotationDegrees = -60f
            ),
        )
    }
}

data class DottedShape(
    val step: Dp,
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ) = Outline.Generic(Path().apply {
        val stepPx = with(density) { step.toPx() }
        val stepsCount = (size.width / stepPx).roundToInt()
        val actualStep = size.width / stepsCount
        val dotSize = Size(width = actualStep / 2, height = size.height)
        for (i in 0 until stepsCount) {
            addRect(
                Rect(
                    offset = Offset(x = i * actualStep, y = 0f),
                    size = dotSize
                )
            )
        }
        close()
    })
}

@Composable
private fun rememberThresholdLine(average : Float): ThresholdLine {
    val line = shapeComponent(strokeWidth = thresholdLineThickness, strokeColor = color2, shape = DottedShape(step = 10.dp))
    return remember(line) {
        ThresholdLine(thresholdValue = average, lineComponent = line, thresholdLabel = "")
    }
}


@Composable
private fun rememberStartAxisLabel() = axisLabelComponent(
    color = Color.Black,
    verticalPadding = 2.dp,
    horizontalPadding = 8.dp,
    verticalMargin = 4.dp,
    horizontalMargin = 4.dp,
    background = shapeComponent(Shapes.roundedCornerShape(4.dp), Color(0xfffab94d)),
)

@Preview
@Composable
fun PreviewFlightDelayGraph() = FlightDelayGraph(
    mutableListOf<String>("03-21", "03-22", "03-23", "03-24", "03-25", "03-26", "03-27", "03-28", "03-29", "03-30"),
    mutableListOf<Int>(1,2, 3, 2, 2, 1, 1, 2, 3,7)
)

private const val COLOR_2_CODE = 0xff91b1fd

val color2 = Color(COLOR_2_CODE)
val thresholdLineThickness = 2.dp