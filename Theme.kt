package mx.itesm.beneficiojoven.view.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

/**
 * Estructura de datos para proveer colores personalizados más allá de los
 * que ofrece MaterialTheme, como los colores para un fondo degradado.
 *
 * @property gradientStart Color de inicio del degradado.
 * @property gradientEnd Color de fin del degradado.
 */
data class ExtendedColors(
    val gradientStart: Color,
    val gradientEnd: Color
)

/**
 * `CompositionLocal` para acceder a la paleta de `ExtendedColors` definida
 * en el tema, permitiendo que cualquier Composable anidado pueda usarla.
 */
val LocalExtendedColors = staticCompositionLocalOf {
    ExtendedColors(
        gradientStart = Color.Unspecified,
        gradientEnd = Color.Unspecified
    )
}

// Paleta de colores para el tema claro
private val LightColorScheme: ColorScheme = lightColorScheme(
    primary = primary,
    onPrimary = onPrimary,
    surface = surface,
    secondary = secondary,
    onSecondary = onSecondary,
    tertiary = tertiary,
    onTertiary = onTertiary,
    outlineVariant = SoftDivider,

    error = error
)

// Paleta de colores para el tema oscuro (puedes personalizarla después)
private val DarkColorScheme: ColorScheme = darkColorScheme()

/**
 * Tema principal de la aplicación BeneficioJoven.
 *
 * Aplica el `MaterialTheme` y, además, provee una paleta de colores extendida
 * (`ExtendedColors`) a través de `CompositionLocalProvider` para componentes
 * personalizados como el fondo degradado.
 *
 * @param darkTheme Indica si se debe usar el esquema de colores oscuro. Por defecto, usa el del sistema.
 * @param dynamicColor Habilita el uso de colores dinámicos en Android 12+. Deshabilitado por defecto.
 * @param content El contenido Composable al que se le aplicará el tema.
 */
@Composable
fun BeneficioJovenTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme =
        if (darkTheme) {
            DarkColorScheme
        } else {
            LightColorScheme
        }

    // Define los valores para los colores del degradado
    val extendedColors = ExtendedColors(
        gradientStart = GradientStart,
        gradientEnd = GradientEnd
    )

    // Provee los colores del degradado al resto de la app
    CompositionLocalProvider(LocalExtendedColors provides extendedColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}
