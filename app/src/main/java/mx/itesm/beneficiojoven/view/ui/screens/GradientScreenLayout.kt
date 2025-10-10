// GradientScreenLayout.kt
package mx.diz.beneficiojovenui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

// "plantilla de pantalla" reutilizable
@Composable
fun GradientScreenLayout(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    // El contenido específico de la pantalla se pasa como un Composable lambda
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF5A749F), Color(0xFF925DFF))
                )
            )
            .padding(contentPadding) // Aplicamos un padding general aquí
    ) {
        content() // Aquí se "inyecta" el contenido de la pantalla
    }
}
