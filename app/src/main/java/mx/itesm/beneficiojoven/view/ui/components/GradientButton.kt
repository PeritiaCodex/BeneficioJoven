package mx.itesm.beneficiojoven.view.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import mx.itesm.beneficiojoven.view.ui.theme.LocalExtendedColors

/**
 * Botón personalizado que usa un gradiente diagonal como fondo cuando está habilitado,
 * y un color sólido y atenuado cuando está deshabilitado.
 *
 * @param onClick La acción a ejecutar cuando se presiona el botón.
 * @param modifier Modificador para personalizar el layout del botón.
 * @param enabled Controla si el botón está activo.
 * @param content El contenido del botón, usualmente un `Text`.
 */
@Composable
fun GradientButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit
) {
    // 1. Obtenemos el gradiente del tema para el estado HABILITADO
    val enabledBrush: Brush = LocalExtendedColors.current.diagonalGradientBrush

    // 2. Definimos un Brush de color sólido para el estado DESHABILITADO
    //    Usamos los colores estándar de Material 3 para superficies deshabilitadas.
    val disabledBrush: Brush = SolidColor(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f))

    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        // Usamos colores transparentes para que no interfieran con nuestro fondo personalizado
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            // El color del texto se gestionará en el Box de abajo para más control
            contentColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = Color.Transparent
        ),
        // Eliminamos el padding por defecto para que nuestro Box ocupe todo el espacio
        contentPadding = PaddingValues()
    ) {
        // 3. Este Box es nuestro fondo real.
        Box(
            modifier = Modifier
                // Aplicamos el fondo correspondiente al estado (enabled/disabled)
                .background(if (enabled) enabledBrush else disabledBrush)
                // Aseguramos que el contenido completo del botón (incluyendo el Box) tenga las esquinas redondeadas
                .then(modifier)
                .padding(horizontal = 16.dp, vertical = 12.dp), // Padding interno para el contenido
            contentAlignment = Alignment.Center,
        ) {
            // 4. Proveemos el color de texto correcto según el estado
            val textColor = if (enabled) {
                MaterialTheme.colorScheme.onSecondary // Texto blanco sobre el gradiente
            } else {
                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f) // Texto gris sobre el fondo deshabilitado
            }

            // Proporcionamos el color de contenido a través de CompositionLocalProvider
            CompositionLocalProvider(LocalContentColor provides textColor) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    content()
                }
            }
        }
    }
}
