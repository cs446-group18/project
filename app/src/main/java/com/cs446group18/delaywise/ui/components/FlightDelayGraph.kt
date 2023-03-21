package com.cs446group18.delaywise.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.axis.axisLabelComponent
import com.patrykandpatrick.vico.compose.axis.horizontal.bottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.startAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.column.columnChart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.component.shape.roundedCornerShape
import com.patrykandpatrick.vico.compose.component.shape.shader.fromBrush
import com.patrykandpatrick.vico.compose.component.shapeComponent
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
import com.patrykandpatrick.vico.core.entry.entryModelOf
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator


@Composable
fun m4ChartStyle(
    axisLabelColor: Color = MaterialTheme.colorScheme.onBackground,
    axisGuidelineColor: Color = MaterialTheme.colorScheme.outline,
    axisLineColor: Color = MaterialTheme.colorScheme.outline,
    entityColors: List<Color> = listOf(
        Color(0xff8fdaff),
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
fun FlightDelayGraph(navigator: DestinationsNavigator, keys: List<String>, values: List<Int>) {
    var chartEntryModel = entryModelOf(*values.toTypedArray())
    val bottomAxisValueFormatter =
        AxisValueFormatter<AxisPosition.Horizontal.Bottom> { x, _ -> keys[x.toInt()] }
    ProvideChartStyle(m3ChartStyle()) {
        Chart(
            chart = columnChart(),
            model = chartEntryModel,
            startAxis = startAxis(
                horizontalLabelPosition = VerticalAxis.HorizontalLabelPosition.Inside,
                label = rememberStartAxisLabel(),
                guideline = null,
                maxLabelCount = 1
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
    navigator = EmptyDestinationsNavigator,
    mutableListOf<String>("03-21", "03-22", "03-23", "03-24", "03-25", "03-26", "03-27", "03-28", "03-29", "03-30"),
    mutableListOf<Int>(1,2, 3, 2, 2, 1, 1, 2, 3,7)
)