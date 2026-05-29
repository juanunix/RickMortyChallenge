package com.example.rickmortychallenge.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.rickmortychallenge.theme.Primary
import com.example.rickmortychallenge.theme.Secondary
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun PortalLoader(
    modifier: Modifier = Modifier,
    size: Dp = 80.dp
) {
    val infiniteTransition = rememberInfiniteTransition(label = "PortalSwirl")
    
    // Slow swirl rotation
    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = androidx.compose.animation.core.RepeatMode.Restart
        ),
        label = "rotation"
    )

    // Pulsing expand scale for organic swirling portal liquid
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = androidx.compose.animation.core.RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer(
                    rotationZ = rotationAngle,
                    scaleX = pulseScale,
                    scaleY = pulseScale
                )
        ) {
            val center = Offset(size.toPx() / 2f, size.toPx() / 2f)
            val radius = size.toPx() / 2f - 8.dp.toPx()

            // Draw background glow portal swirl (translucent secondary/primary green)
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Primary.copy(alpha = 0.4f),
                        Secondary.copy(alpha = 0.15f),
                        Color.Transparent
                    ),
                    center = center,
                    radius = radius * 1.2f
                )
            )

            // Draw Swirling arcs resembling portal fluid
            val numArcs = 4
            for (i in 0 until numArcs) {
                val startAngle = (i * (360f / numArcs))
                val sweepAngle = 70f
                
                // Color variation along the portal swirl
                val sweepColor = if (i % 2 == 0) Primary else Secondary

                drawArc(
                    brush = Brush.sweepGradient(
                        colors = listOf(
                            sweepColor.copy(alpha = 0.1f),
                            sweepColor,
                            sweepColor.copy(alpha = 0.1f)
                        ),
                        center = center
                    ),
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    style = Stroke(
                        width = 4.dp.toPx(),
                        cap = androidx.compose.ui.graphics.StrokeCap.Round
                    )
                )
            }

            // Draw inner radioactive bubbles or vortex lines
            for (j in 0..5) {
                val angle = rotationAngle * (if (j % 2 == 0) 1.5f else -1f) + (j * 60f)
                val angleRad = Math.toRadians(angle.toDouble())
                val dotRadius = 3.dp.toPx()
                val offsetDist = radius * 0.5f * (1f - (j * 0.1f))
                
                drawCircle(
                    color = Primary,
                    radius = dotRadius,
                    center = Offset(
                        x = center.x + (cos(angleRad) * offsetDist).toFloat(),
                        y = center.y + (sin(angleRad) * offsetDist).toFloat()
                    ),
                    alpha = 0.8f
                )
            }
        }
    }
}
