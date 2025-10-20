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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext


private val BrownSoft          = Color(0xFF8D6E63) // opcional: secondary
private val GreenAccent        = Color(0xFF1B5E20) // opcional: tertiary

// Paleta clara con los colores del mock
private val LightColorScheme: ColorScheme = lightColorScheme(
    primary = AppBarYellow,
    onPrimary = AppBarText,
    background = AppBackgroundCream,
    surface = AppBackgroundCream,
    secondary = BrownSoft,
    tertiary = GreenAccent,
    outlineVariant = SoftDivider
)

// Deja oscuro por defecto
private val DarkColorScheme: ColorScheme = darkColorScheme()

@Composable
fun BeneficioJovenTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme =
        if (darkTheme) {
            if (dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                dynamicDarkColorScheme(LocalContext.current)
            } else {
                DarkColorScheme
            }
        } else {
            if (dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                dynamicLightColorScheme(LocalContext.current).copy(
                    primary = AppBarYellow,
                    onPrimary = AppBarText,
                    background = AppBackgroundCream,
                    surface = AppBackgroundCream,
                    secondary = BrownSoft,
                    tertiary = GreenAccent,
                    outlineVariant = SoftDivider
                )
            } else {
                LightColorScheme
            }
        }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
