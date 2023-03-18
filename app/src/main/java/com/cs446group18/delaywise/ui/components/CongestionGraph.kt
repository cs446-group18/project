package com.cs446group18.delaywise.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import com.patrykandpatrick.vico.compose.axis.horizontal.bottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.startAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.component.shape.shader.fromBrush
import com.patrykandpatrick.vico.compose.component.shapeComponent
import com.patrykandpatrick.vico.compose.component.textComponent
import com.patrykandpatrick.vico.compose.style.ChartStyle
import com.patrykandpatrick.vico.compose.style.ProvideChartStyle
import com.patrykandpatrick.vico.core.DefaultAlpha
import com.patrykandpatrick.vico.core.DefaultColors
import com.patrykandpatrick.vico.core.DefaultDimens
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.axis.horizontal.HorizontalAxis
import com.patrykandpatrick.vico.core.axis.vertical.VerticalAxis
import com.patrykandpatrick.vico.core.chart.line.LineChart
import com.patrykandpatrick.vico.core.component.shape.LineComponent
import com.patrykandpatrick.vico.core.component.shape.Shapes
import com.patrykandpatrick.vico.core.component.shape.shader.DynamicShaders
import com.patrykandpatrick.vico.core.dimensions.MutableDimensions
import com.patrykandpatrick.vico.core.entry.entryModelOf
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import java.util.*
import kotlin.random.Random


private val rightNow = Calendar.getInstance()
private val currentHourIn24Format: Int = rightNow.get(Calendar.HOUR_OF_DAY)
private val times = mutableListOf<String>()
fun append_times() {
    for (i in 6 downTo 0 step 1) {
        val curr = currentHourIn24Format - i
        times.add((if (curr % 12 === 0) "12" else (curr % 12)).toString() + if (curr >= 12) "PM" else "AM")
    }
}

@Composable
fun m3ChartStyle(
    axisLabelColor: Color = MaterialTheme.colorScheme.onBackground,
    axisGuidelineColor: Color = MaterialTheme.colorScheme.outline,
    axisLineColor: Color = MaterialTheme.colorScheme.outline,
    entityColors: List<Color> = listOf(
        MaterialTheme.colorScheme.primary,
        MaterialTheme.colorScheme.secondary,
        MaterialTheme.colorScheme.tertiary,
    ),
    elevationOverlayColor: Color = MaterialTheme.colorScheme.primary
): ChartStyle {
    return ChartStyle(
        ChartStyle.Axis(
            axisLabelColor = axisLabelColor,
            axisGuidelineColor = axisGuidelineColor,
            axisLineColor = axisLineColor,
        ),
        ChartStyle.ColumnChart(
            entityColors.map { columnChartColor ->
                LineComponent(
                    columnChartColor.toArgb(),
                    DefaultDimens.COLUMN_WIDTH,
                    Shapes.roundedCornerShape(DefaultDimens.COLUMN_ROUNDNESS_PERCENT),
                )
            },
        ),
        ChartStyle.LineChart(
            entityColors.map { lineChartColor ->
                LineChart.LineSpec(
                    lineColor = lineChartColor.toArgb(),
                    lineBackgroundShader = DynamicShaders.fromBrush(
                        Brush.verticalGradient(
                            listOf(
                                lineChartColor.copy(DefaultAlpha.LINE_BACKGROUND_SHADER_START),
                                lineChartColor.copy(DefaultAlpha.LINE_BACKGROUND_SHADER_END),
                            ),
                        ),
                    ),
                )
            },
        ),
        ChartStyle.Marker(),
        Color(DefaultColors.Light.elevationOverlayColor),
    )
}

@Composable
fun CongestionGraph(navigator: DestinationsNavigator) {
    append_times()
    val scope = rememberCoroutineScope()
    val randList = List(7) { Random.nextInt(0, 180) }
    var chartEntryModel = entryModelOf(*randList.toTypedArray())
    val bottomAxisValueFormatter =
        AxisValueFormatter<AxisPosition.Horizontal.Bottom> { x, _ -> times[x.toInt() % times.size] }
    ProvideChartStyle(m3ChartStyle()) {
        Chart(
            chart = lineChart(
                pointPosition = LineChart.PointPosition.Start
            ),
            model = chartEntryModel,
            startAxis = startAxis(
                horizontalLabelPosition = VerticalAxis.HorizontalLabelPosition.Inside,
                titleComponent = textComponent(
                    padding = MutableDimensions(4F, 2F),
                    color = Color.DarkGray,
                    background = shapeComponent(Shapes.pillShape, Color.LightGray),
                ),
                guideline = null,
                title = "Delay (Min)",
                maxLabelCount = 1
            ),
            bottomAxis = bottomAxis(
                titleComponent = textComponent(
                    padding = MutableDimensions(4F, 2F),
                    color = Color.DarkGray,
                    background = shapeComponent(Shapes.pillShape, Color.LightGray),
                ),
                title = "Hour",
                guideline = null,
                tickPosition = HorizontalAxis.TickPosition.Center(0, 1),
                valueFormatter = bottomAxisValueFormatter
            ),
        )
    }
}

@Preview
@Composable
fun PreviewCongestionGraph() = CongestionGraph(
    navigator = EmptyDestinationsNavigator
)