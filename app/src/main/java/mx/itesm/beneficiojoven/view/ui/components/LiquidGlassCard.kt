package mx.itesm.beneficiojoven.view.ui.components

import android.os.Build
import android.graphics.Shader
import android.graphics.RenderEffect as FwRenderEffect
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import mx.itesm.beneficiojoven.view.ui.screens.LocalBackdropColors
import mx.itesm.beneficiojoven.view.ui.screens.LocalScreenHeightPx

@Composable
fun LiquidGlassCard(
    modifier: Modifier = Modifier,
    shape: RoundedCornerShape = RoundedCornerShape(22.dp),
    cornerRadius: Dp = 22.dp,
    blurRadius: Dp = 18.dp,                 // “Casi Cristal”
    tint: Color = Color.White,
    tintAlpha: Float = 0.04f,               // “Casi Cristal”
    backdropAlpha: Float = 0.95f,           // “Casi Cristal”
    borderAlpha: Float = 0.22f,             // “Casi Cristal”
    highlightAlpha: Float = 0.10f,          // “Casi Cristal”
    content: @Composable BoxScope.() -> Unit
) {
    val density = LocalDensity.current
    val radiusPx = with(density) { cornerRadius.toPx() }
    val blurPx   = with(density) { blurRadius.toPx() }

    // Posición global (para alinear el gradiente al fondo real)
    var topInRoot by remember { mutableStateOf(0f) }
    val screenH = LocalScreenHeightPx.current
    val backdropColors = LocalBackdropColors.current

    // Brillo “líquido”
    val infinite = rememberInfiniteTransition(label = "liquid")
    val sweep by infinite.animateFloat(
        initialValue = -0.2f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "liquid"
    )


    val borderBrush = Brush.linearGradient(
        listOf(Color.White.copy(alpha = borderAlpha), Color.White.copy(alpha = 0.06f))
    )
    val glassTint = Brush.linearGradient(
        listOf(tint.copy(alpha = tintAlpha + 0.02f), tint.copy(alpha = tintAlpha))
    )

    Box(
        modifier = modifier
            .onGloballyPositioned { topInRoot = it.positionInRoot().y }
            .clip(shape)
    ) {

        // 1) Fondo alineado al gradiente GLOBAL + blur
        Box(
            Modifier
                .matchParentSize()
                .clip(shape)
                .drawBehind {
                    val startY = -topInRoot              // alinear a coordenadas globales
                    val endY = startY + screenH
                    drawRoundRect(
                        brush = Brush.verticalGradient(
                            colors = backdropColors,
                            startY = startY,
                            endY = endY
                        ),
                        cornerRadius = CornerRadius(radiusPx, radiusPx),
                        alpha = backdropAlpha
                    )
                }
                .graphicsLayer {
                    compositingStrategy = CompositingStrategy.Offscreen
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        renderEffect = FwRenderEffect
                            .createBlurEffect(blurPx, blurPx, Shader.TileMode.DECAL)
                            .asComposeRenderEffect()
                    }
                }
        )

        // 2) Capa de vidrio (velo + brillos + borde)
        Box(
            Modifier
                .matchParentSize()
                .clip(shape)
                .drawBehind {
                    // Velo
                    drawRoundRect(
                        brush = glassTint,
                        cornerRadius = CornerRadius(radiusPx, radiusPx),
                        blendMode = BlendMode.SrcOver
                    )
                    // Highlight fijo
                    drawRoundRect(
                        brush = Brush.linearGradient(
                            listOf(Color.White.copy(alpha = 0.12f), Color.Transparent)
                        ),
                        cornerRadius = CornerRadius(radiusPx, radiusPx),
                        blendMode = BlendMode.Overlay
                    )
                    // Highlight animado
                    val cx = size.width * sweep
                    drawRoundRect(
                        brush = Brush.radialGradient(
                            listOf(Color.White.copy(alpha = highlightAlpha), Color.Transparent),
                            center = Offset(cx, size.height * 0.25f),
                            radius = size.minDimension * 0.85f
                        ),
                        cornerRadius = CornerRadius(radiusPx, radiusPx),
                        blendMode = BlendMode.Overlay
                    )
                    // Borde luminoso
                    drawRoundRect(
                        brush = borderBrush,
                        cornerRadius = CornerRadius(radiusPx, radiusPx),
                        style = Stroke(width = 1.dp.toPx())
                    )
                }
        )

        // 3) Contenido (sin blur)
        Box(Modifier.matchParentSize()) {
            androidx.compose.material3.ProvideTextStyle(
                value = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.White.copy(alpha = 0.92f)
                )
            ) { content() }
        }
    }
}
