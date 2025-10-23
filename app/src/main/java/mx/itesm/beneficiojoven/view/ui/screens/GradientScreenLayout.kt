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
import mx.itesm.beneficiojoven.view.ui.theme.LocalExtendedColors

/**
 * Archivo que define el layout de pantalla principal con un fondo degradado.
 *
 * Proporciona un `CompositionLocal` para que los componentes hijos puedan
 * acceder a información del fondo, como el `Brush` y los colores.
 */

/**
 * `CompositionLocal` que provee la lista de colores (`List<Color>`) utilizada
 * para crear el fondo degradado. Es útil si un componente hijo necesita
 * reaccionar o usar los mismos colores del fondo.
 */
val LocalBackdropColors = staticCompositionLocalOf { listOf(Color(0xFF84FCEE), Color(0xFF925DFF)) }

/**
 * `CompositionLocal` que provee la altura total de la pantalla en píxeles.
 */
val LocalScreenHeightPx = staticCompositionLocalOf { 0f }

/**
 * Plantilla reutilizable de pantalla con un **fondo degradado vertical** que
 * consume los colores desde `LocalExtendedColors` del tema.
 *
 * Envuelve al [content] y le aplica un `padding`. Además, provee a sus hijos
 * el `Brush` del fondo, la lista de colores y la altura de la pantalla a través
 * de `CompositionLocalProvider`.
 *
 * @param modifier Modificador opcional para el contenedor raíz.
 * @param contentPadding Márgenes internos aplicados al contenido (por defecto `16.dp`).
 * @param content Contenido específico de la pantalla que se mostrará sobre el degradado.
 */
@Composable
fun GradientScreenLayout(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    content: @Composable () -> Unit
) {
    val extendedColors = LocalExtendedColors.current
    val colors = listOf(extendedColors.gradientStart, extendedColors.gradientEnd)
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