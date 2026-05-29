package com.example.rickmortychallenge.ui.components

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.rickmortychallenge.theme.Primary

fun Modifier.portalGlow(
    color: Color = Primary,
    alpha: Float = 0.20f,
    borderRadius: Dp = 16.dp,
    glowRadius: Dp = 12.dp,
    offsetY: Dp = 0.dp,
    offsetX: Dp = 0.dp
) = this.drawBehind {
    val glowColor = color.copy(alpha = alpha).toArgb()
    val paint = Paint().apply {
        val frameworkPaint = asFrameworkPaint()
        frameworkPaint.color = glowColor
        frameworkPaint.setShadowLayer(
            glowRadius.toPx(),
            offsetX.toPx(),
            offsetY.toPx(),
            glowColor
        )
    }
    
    drawIntoCanvas { canvas ->
        canvas.drawRoundRect(
            left = 0f,
            top = 0f,
            right = size.width,
            bottom = size.height,
            radiusX = borderRadius.toPx(),
            radiusY = borderRadius.toPx(),
            paint = paint
        )
    }
}
