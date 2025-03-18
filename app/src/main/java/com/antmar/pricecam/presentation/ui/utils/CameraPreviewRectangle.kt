package com.antmar.pricecam.presentation.ui.utils

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun CameraPreviewRectangle() {

    Canvas(modifier = Modifier.fillMaxSize()) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        val frameWidth = 330.dp.toPx()
        val frameHeight = 250.dp.toPx()

        val left = (canvasWidth - frameWidth) / 2
        val top = (canvasHeight - frameHeight) / 2
        val right = left + frameWidth
        val bottom = top + frameHeight

        val path = Path().apply {
            addRect(Rect(0f, 0f, canvasWidth, canvasHeight))
            addRect(Rect(left, top, right, bottom))
            fillType = PathFillType.EvenOdd
        }

        val dashedPath = Path().apply {
            moveTo(left, top)
            lineTo(right, top)
            lineTo(right, bottom)
            lineTo(left, bottom)
            lineTo(left, top)
        }

        val dashedEffect = PathEffect.dashPathEffect(
            intervals = floatArrayOf(5.dp.toPx(), 10.dp.toPx()), phase = 0f
        )

        drawPath(
            path = path,
            color = androidx.compose.ui.graphics.Color.Black.copy(0.5f),
            blendMode = BlendMode.SrcOver
        )

        drawPath(
            path = dashedPath,
            color = androidx.compose.ui.graphics.Color.White.copy(0.4f),
            style = Stroke(
                width = 2.dp.toPx(), pathEffect = dashedEffect
            )
        )
    }
}