package mx.itesm.beneficiojoven.view.ui.screens

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

/**
 * Plantilla reutilizable de pantalla con **fondo degradado vertical**.
 *
 * Envuelve al [content] y le aplica un `padding` consistente ([contentPadding]).
 *
 * @param modifier Modificador opcional para el contenedor raíz.
 * @param contentPadding Márgenes internos aplicados al contenido (por defecto `16.dp`).
 * @param content Contenido específico de la pantalla.
 */
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
