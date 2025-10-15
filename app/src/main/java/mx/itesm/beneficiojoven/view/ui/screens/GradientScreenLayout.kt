package mx.itesm.beneficiojoven.view.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import mx.itesm.beneficiojoven.view.ui.components.LocalBackdropBrush

/**
 * Plantilla reutilizable de pantalla con **fondo degradado vertical**.
 *
 * Envuelve al [content] y le aplica un `padding` consistente ([contentPadding]).
 *
 * @param modifier Modificador opcional para el contenedor raíz.
 * @param contentPadding Márgenes internos aplicados al contenido (por defecto `16.dp`).
 * @param content Contenido específico de la pantalla.
 */


val LocalBackdropColors = staticCompositionLocalOf { listOf(Color(0xFF9EBCEC), Color(0xFF925DFF)) }
val LocalScreenHeightPx = staticCompositionLocalOf { 0f }
@Composable
fun GradientScreenLayout(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    // El contenido específico de la pantalla se pasa como un Composable lambda
    content: @Composable () -> Unit
) {
    val colors = listOf(Color(0xFF9EBCEC), Color(0xFF925DFF))
    val bgBrush = Brush.verticalGradient(colors)

    val conf = LocalConfiguration.current
    val density = LocalDensity.current
    val screenHeightPx = with(density) { conf.screenHeightDp.dp.toPx() }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(bgBrush)
            .padding(contentPadding)
    ) {
        CompositionLocalProvider(
            LocalBackdropBrush provides bgBrush,
            LocalBackdropColors provides colors,
            LocalScreenHeightPx provides screenHeightPx
        ) {
            content()
        }
    }
}