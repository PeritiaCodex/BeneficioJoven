package mx.itesm.beneficiojoven.view.ui.components

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier

val LocalBackdropBrush = staticCompositionLocalOf<Brush> {
    // fallback por si no se provee desde el layout
    Brush.verticalGradient(listOf(Color(0xFFFFFFFF), Color(0xFFD9CAF6)))
}

@Composable
fun GradientScreenLayout(
    content: @Composable () -> Unit
) {
    val brush = Brush.verticalGradient(
        0f to Color(0xFF6A78FF),
        1f to Color(0xFFB86BFF)
    )
    Box(
        Modifier
            .fillMaxSize()
            .background(brush)
    ) {
        CompositionLocalProvider(LocalBackdropBrush provides brush) {
            content()
        }
    }
}
