package com.plcoding.cryptotracker.crypto.presentation.coindetail.chart

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.plcoding.cryptotracker.crypto.domain.models.CoinHistory
import com.plcoding.cryptotracker.ui.theme.CryptoTrackerTheme
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt
import kotlin.random.Random

@Composable
fun LineChart(
    dataPoints: List<DataPoint>,
    chartStyle: ChartStyle,
    visibleDataPointsIndices: IntRange,
    unit: String,
    selectedDataPoint: DataPoint? = null,
    showHelperLines: Boolean = true,
    onSelectedDataPoint: (DataPoint) -> Unit = {},
    onXLabelWidthChange: (Float) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val textStyle = LocalTextStyle.current.copy(
        fontSize = chartStyle.labelFontSize
    )

    val visibleDataPoints = remember(key1 = dataPoints, key2 = visibleDataPointsIndices) {
        dataPoints.slice(visibleDataPointsIndices)
    }

    val maxYValue = remember(visibleDataPoints) {
        visibleDataPoints.maxOfOrNull { it.y } ?: 0f
    }

    val minYValue = remember(visibleDataPoints) {
        visibleDataPoints.minOfOrNull { it.y } ?: 0f
    }

    val measurer = rememberTextMeasurer()

    var xLabelWidth by remember {
        mutableFloatStateOf(0f)
    }

    LaunchedEffect(xLabelWidth) {
        onXLabelWidthChange(xLabelWidth)
    }

    val selectedDataPointIndex = remember(selectedDataPoint) {
        dataPoints.indexOf(selectedDataPoint)
    }

    var drawPoints by remember {
        mutableStateOf(listOf<DataPoint>())
    }

    var isShowingDataPoints by remember {
        mutableStateOf(selectedDataPoint != null)
    }

    Canvas(modifier = modifier
        .fillMaxSize()
        .pointerInput(drawPoints, xLabelWidth) {
            detectHorizontalDragGestures { change, _ ->

                val newSelectedDataPointIndex = getSelectedDataPointIndex(
                    touchOffsetX = change.position.x,
                    triggerWidth = xLabelWidth,
                    drawPoints = drawPoints
                )

                isShowingDataPoints =
                    (newSelectedDataPointIndex + visibleDataPointsIndices.first) in visibleDataPointsIndices

                if (isShowingDataPoints) {
                    onSelectedDataPoint(dataPoints[newSelectedDataPointIndex])
                }
            }

        }) {
        val minYLabelSpacingPx = chartStyle.minYLabelSpacing.toPx()
        val verticalPaddingPx = chartStyle.verticalPadding.toPx()
        val horizontalPaddingPx = chartStyle.horizontalPadding.toPx()
        val xAxisLabelSpacingPx = chartStyle.xAxisLabelSpacing.toPx()

        val xLabelTextLayoutResults = visibleDataPoints.map {
            measurer.measure(
                text = it.xLabel, style = textStyle.copy(textAlign = TextAlign.Center)
            )
        }

        val maxXLabelWidth = xLabelTextLayoutResults.maxOf { it.size.width }
        val maxXLabelHeight = xLabelTextLayoutResults.maxOf { it.size.height }
        val maxXLabelLineCount = xLabelTextLayoutResults.maxOf { it.lineCount }
        val xLabelLineHeight = maxXLabelHeight / maxXLabelLineCount

        val viewPortHeightPx =
            size.height - (maxXLabelHeight + 2f * verticalPaddingPx + xLabelLineHeight + xAxisLabelSpacingPx)

        // Y Label Calculation
        val labelViewPortHeightPx = viewPortHeightPx + xLabelLineHeight
        val labelCount = (labelViewPortHeightPx / (xLabelLineHeight + minYLabelSpacingPx)).toInt()
        val valueIncrement = (maxYValue - minYValue) / labelCount

        val yLabels = (0..labelCount).map { i ->
            ValueLabel(
                value = maxYValue - (valueIncrement * i), unit = unit
            )
        }

        val yLabelsTextLayoutResults = yLabels.map {
            measurer.measure(
                text = it.formatted(), style = textStyle
            )
        }

        val maxYLabelWidth = yLabelsTextLayoutResults.maxOf { it.size.width }

        val viewPortTopY = verticalPaddingPx + xLabelLineHeight + 10f
        val viewPortRightX = size.width
        val viewPortBottomY = viewPortTopY + viewPortHeightPx
        val viewPortLeftX = 2f * horizontalPaddingPx + maxYLabelWidth

        xLabelWidth = maxXLabelWidth + xAxisLabelSpacingPx
        xLabelTextLayoutResults.forEachIndexed { index, textLayout ->
            val color = if (index == selectedDataPointIndex) {
                chartStyle.selectedColor
            } else {
                chartStyle.unselectedColor
            }
            val xLabel = viewPortLeftX + xAxisLabelSpacingPx / 2 + (index * xLabelWidth)

            drawText(
                textLayoutResult = textLayout, topLeft = Offset(
                    x = xLabel, y = viewPortBottomY + xAxisLabelSpacingPx
                ), color = color
            )
            val xLine = xLabel + textLayout.size.width / 2
            if (showHelperLines) {
                drawLine(
                    color = color,
                    start = Offset(x = xLine, y = viewPortTopY),
                    end = Offset(x = xLine, y = viewPortBottomY),
                    strokeWidth = chartStyle.helperLinesThicknessPx
                )
            }

            if (selectedDataPointIndex == index) {
                val valueLabel = ValueLabel(
                    value = visibleDataPoints[index].y, unit = unit
                )

                val valueLabelTextResult = measurer.measure(
                    text = valueLabel.formatted(), style = textStyle.copy(
                        color = chartStyle.selectedColor
                    ), maxLines = 1
                )

                val textPositionX = if (selectedDataPointIndex == visibleDataPointsIndices.last) {
                    xLine - textLayout.size.width
                } else {
                    xLine - textLayout.size.width / 2
                }

                val isTextInVisibleRange =
                    (size.width - textPositionX).roundToInt() in 0..size.width.roundToInt()

                if (isTextInVisibleRange) {
                    drawText(
                        textLayoutResult = valueLabelTextResult, topLeft = Offset(
                            x = textPositionX,
                            y = viewPortTopY - valueLabelTextResult.size.height - 10
                        )
                    )
                }
            }

        }

        val heightRequiredForLabels = xLabelLineHeight * (labelCount + 1)
        val remainingHeightForLabels = labelViewPortHeightPx - heightRequiredForLabels
        val spaceBetweenLabels = remainingHeightForLabels / labelCount

        yLabelsTextLayoutResults.forEachIndexed { index, textLayout ->
            val x = horizontalPaddingPx + maxYLabelWidth - textLayout.size.width
            val y =
                viewPortTopY + index * (xLabelLineHeight + spaceBetweenLabels) - xLabelLineHeight / 2
            drawText(
                textLayoutResult = textLayout, topLeft = Offset(
                    x = x, y = y
                ), color = chartStyle.unselectedColor
            )

            if (showHelperLines) {
                val yLine = y + textLayout.size.height / 2
                drawLine(
                    color = chartStyle.unselectedColor,
                    start = Offset(x = viewPortLeftX, y = yLine),
                    end = Offset(x = viewPortRightX, y = yLine),
                    strokeWidth = chartStyle.helperLinesThicknessPx
                )
            }
        }

        drawPoints = visibleDataPointsIndices.map {
            val x =
                viewPortLeftX + (it - visibleDataPointsIndices.first) * xLabelWidth + xLabelWidth / 2f

            val ratio = (dataPoints[it].y - minYValue) / (maxYValue - minYValue)
            val y = viewPortBottomY - (viewPortHeightPx * ratio)

            DataPoint(x = x, y = y, xLabel = dataPoints[it].xLabel)
        }

        val conPoints1 = mutableListOf<DataPoint>()
        val conPoints2 = mutableListOf<DataPoint>()

        for (i in 1 until drawPoints.size) {
            val p0 = drawPoints[i - 1]
            val p1 = drawPoints[i]

            val x = (p0.x + p1.x) / 2
            val y1 = p0.y
            val y2 = p1.y

            conPoints1.add(DataPoint(x = x, y = y1, xLabel = ""))
            conPoints2.add(DataPoint(x = x, y = y2, xLabel = ""))
        }

        val linePath = Path().apply {
            if (drawPoints.isNotEmpty()) {
                val firstDrawPoint = drawPoints.first()
                moveTo(x = firstDrawPoint.x, y = firstDrawPoint.y)

                for (i in 1 until drawPoints.size) {
                    val conPoint1 = conPoints1[i - 1]
                    val conPoint2 = conPoints2[i - 1]
                    cubicTo(
                        x1 = conPoint1.x,
                        y1 = conPoint1.y,
                        x2 = conPoint2.x,
                        y2 = conPoint2.y,
                        x3 = drawPoints[i].x,
                        y3 = drawPoints[i].y
                    )
                }
            }
        }

        drawPath(
            path = linePath, color = chartStyle.selectedColor, style = Stroke(
                width = 5f, cap = StrokeCap.Round
            )
        )

        drawPoints.forEachIndexed { index, dataPoint ->
            if (isShowingDataPoints) {
                var radius = 10f
                var color = chartStyle.selectedColor
                if (selectedDataPointIndex == index) {
                    radius = 15f
                    color = chartStyle.selectedColor
                }
                drawCircle(
                    color = color,
                    radius = radius,
                    center = Offset(x = dataPoint.x, y = dataPoint.y),
                )
            }
        }
    }
}

private fun getSelectedDataPointIndex(
    touchOffsetX: Float, triggerWidth: Float, drawPoints: List<DataPoint>
): Int {
    val triggerRangeLeft = touchOffsetX - triggerWidth / 2
    val triggerRangeRight = touchOffsetX + triggerWidth / 2

    println("${drawPoints.size}")
    println("${triggerRangeLeft}, ${triggerRangeRight}. ${drawPoints.map { "x=${it.x}-y=${it.y}" }}")

    return drawPoints.indexOfFirst { it.x in triggerRangeLeft..triggerRangeRight }

}

@Preview
@Composable
private fun LineChartPreview() {
    CryptoTrackerTheme {
        val coinHistoryRandomized = remember {
            (1..20).map { i ->
                CoinHistory(
                    priceUsd = Random.nextDouble(),
                    dateTime = ZonedDateTime.now().plusHours(i.toLong())
                )
            }
        }

        val style = ChartStyle(
            chartLineColor = Color.Black,
            unselectedColor = Color(0xFF7C7C7C),
            selectedColor = Color.Black,
            helperLinesThicknessPx = 1f,
            axisLinesThicknessPx = 5f,
            labelFontSize = 14.sp,
            minYLabelSpacing = 25.dp,
            verticalPadding = 8.dp,
            horizontalPadding = 8.dp,
            xAxisLabelSpacing = 8.dp
        )

        val dataPoints = remember {
            coinHistoryRandomized.map {
                DataPoint(
                    x = it.dateTime.hour.toFloat(),
                    y = it.priceUsd.toFloat(),
                    xLabel = DateTimeFormatter.ofPattern("ha\nM/d").format(it.dateTime)
                )
            }
        }

        LineChart(
            dataPoints = dataPoints,
            chartStyle = style,
            visibleDataPointsIndices = 17..19,
            unit = "$",
            modifier = Modifier
                .width(700.dp)
                .height(300.dp)
                .background(Color.White),
            selectedDataPoint = dataPoints[1]
        )
    }
}