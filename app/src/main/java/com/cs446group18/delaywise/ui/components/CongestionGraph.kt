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
import com.patrykandpatrick.vico.compose.style.ChartStyle
import com.patrykandpatrick.vico.compose.style.ProvideChartStyle
import com.patrykandpatrick.vico.core.DefaultAlpha
import com.patrykandpatrick.vico.core.DefaultColors
import com.patrykandpatrick.vico.core.DefaultDimens
import com.patrykandpatrick.vico.core.chart.line.LineChart
import com.patrykandpatrick.vico.core.component.shape.LineComponent
import com.patrykandpatrick.vico.core.component.shape.Shapes
import com.patrykandpatrick.vico.core.component.shape.shader.DynamicShaders
import com.patrykandpatrick.vico.core.entry.entryModelOf
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator

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
    val scope = rememberCoroutineScope()
    val chartEntryModel = entryModelOf(4f, 12f, 8f, 16f)
    ProvideChartStyle(m3ChartStyle()) {
        Chart(
            chart = lineChart(),
            model = chartEntryModel,
            startAxis = startAxis(),
            bottomAxis = bottomAxis(),
        )
    }
}

@Preview
@Composable
fun PreviewCongestionGraph() = CongestionGraph(
    navigator = EmptyDestinationsNavigator
)