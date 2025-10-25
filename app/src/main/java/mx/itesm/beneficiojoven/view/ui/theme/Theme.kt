package mx.itesm.beneficiojoven.view.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// --- Definición de la Paleta de Colores Extendida ---
data class ExtendedColors(
    val gradientStart: Color,
    val gradientEnd: Color,
    val diagonalGradientBrush: Brush
)

val LocalExtendedColors = staticCompositionLocalOf {
    ExtendedColors(
        gradientStart = Color.Unspecified,
        gradientEnd = Color.Unspecified,
        diagonalGradientBrush = Brush.horizontalGradient()
    )
}

// --- Paleta de Colores para el Tema Claro (Light Theme) ---
private val LightColorScheme = lightColorScheme(
    //---Contenedores principales---
    primaryContainer = Color(0xFFEBE7F4), // Tono claro de `primary` para contenedores.
    onPrimaryContainer = Color(0xFF825CB1),// Texto/iconos sobre `primaryContainer`.

    // Primarios
    primary = Color(0xFF1C2532),           // Boton moradillo''
    onPrimary = Color(0xFF1C2532),               // Texto/iconos sobre 'primary'. Se recomienda blanco para contraste.

    // Botón Interactivo
    secondary = Color(0xFF62419A),         // Tu 'logo 3', para acentos y FABs.
    onSecondary = Color.White,             // Texto/iconos sobre 'secondary'.

    // Roles Terciarios
    tertiary = Color(0xFFE8ACFD),          // Tu 'tertiary', para balance o atención.
    onTertiary = Color(0xFFE0BEFF),              // Texto/iconos sobre 'tertiary'.

    // Roles de Error
    error = Color(0xFFD35090),
    onError = Color.White,

    // Roles de Superficie y Fondo
    background = Color(0xFFF1F2F4),        // El color de fondo general de la app.
    onBackground = Color(0xFF1F2937),      // Texto sobre el fondo.

    surface = Color(0xFFFFFFFF),           // Color para Cards, Sheets, Menus. Blanco es lo estándar.
    onSurface = Color(0xFF1F2937),         // Título , color principal de texto sobre 'surface'.
    onSurfaceVariant = Color(0xFF374050),   // Subtítulo', para texto con menor énfasis.

    surfaceTint = Color(0xFF6F3878),       // Usa el color primario para la elevación tonal.
    outline = Color(0xFF7A68FD),           // Tu 'outlineVariant', para bordes con contraste.
    scrim = Color.Black                    // Color estándar para oscurecer contenido.
)

// TODO: Definir una paleta de colores coherente para el Tema Oscuro.
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFD0BCFF),
    onPrimary = Color(0xFF381E72),
    secondary = Color(0xFFCCC2DC),
    onSecondary = Color(0xFF332D41),
    tertiary = Color(0xFFEFB8C8),
    onTertiary = Color(0xFF492532),
    background = Color(0xFF1C1B1F),
    onBackground = Color(0xFFE6E1E5),
    surface = Color(0xFF1C1B1F),
    onSurface = Color(0xFFE6E1E5)
)

/**
 * Tema principal de la aplicación BeneficioJoven.
 */
@Composable
fun BeneficioJovenTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    // Define y provee los colores personalizados para el gradiente
    val extendedColors = ExtendedColors(
        gradientStart = GradientStart,
        gradientEnd = GradientEnd,
        diagonalGradientBrush = Brush.linearGradient(
            colors = listOf(
                Color(0xFF3f63a0),
                Color(0xFF5b53a3),
                Color(0xFF6b47a7)
            ),
            start = Offset(0f, Float.POSITIVE_INFINITY), // Bottom-Left
            end = Offset(Float.POSITIVE_INFINITY, 0f)    // Top-Right
        )
    )

    CompositionLocalProvider(LocalExtendedColors provides extendedColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}
