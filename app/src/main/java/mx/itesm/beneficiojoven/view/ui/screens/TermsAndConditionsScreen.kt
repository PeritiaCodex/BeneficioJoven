package mx.itesm.beneficiojoven.view.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp

/**
 * Pantalla de **Términos y Condiciones**.
 *
 * Muestra un fondo con degradado, encabezado con botón de regreso y un cuerpo de texto
 * desplazable dentro de una tarjeta translúcida. El contenido es estático (placeholder).
 *
 * @param onBack Acción para cerrar la pantalla y volver a la anterior.
 */
@Composable
fun TermsScreen(
    onBack: () -> Unit = {},
) {
    // Se elimina el padding del layout principal para controlar el layout interno
    GradientScreenLayout(contentPadding = PaddingValues(all = 0.dp)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // CORRECCIÓN: Encabezado consistente con las otras pantallas (flecha a la izquierda, título centrado)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 4.dp, end = 48.dp), // Padding para centrar el título
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Regresar",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(32.dp)
                    )
                }
                Text(
                    text = "Términos y Condiciones",
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Contenedor con texto desplazable
            Surface(
                shape = CardDefaults.shape,
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.85f),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 8.dp) // Padding exterior para la tarjeta
            ) {
                // Box con scroll para el texto
                Box(
                    modifier = Modifier
                        .padding(16.dp) // Padding interior para el texto
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        // Usamos buildAnnotatedString para formatear el texto.
                        text = buildAnnotatedString {
                            append("Bienvenido a Beneficio Joven. Al usar nuestra aplicación, aceptas los siguientes términos:\n\n")

                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append("1. Aceptación de los términos\n")
                            }
                            append("Al registrarte y utilizar la aplicación Beneficio Joven, aceptas cumplir con los presentes Términos y Condiciones de uso. El acceso y uso de esta app está destinado exclusivamente a jóvenes beneficiarios registrados por el municipio de Atizapán.\n\n")

                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append("2. Uso autorizado\n")
                            }
                            append("La app debe utilizarse únicamente para consultar, redimir y administrar cupones dentro del programa Beneficio Joven. Queda estrictamente prohibido manipular la app, compartir accesos personales o realizar usos indebidos del sistema.\n\n")

                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append("3. Protección de datos personales\n")
                            }
                            append("Los datos proporcionados (nombre, CURP, correo electrónico, municipio, etc.) serán tratados conforme a la Ley Federal de Protección de Datos Personales en Posesión de los Particulares. Se utilizarán exclusivamente para fines del programa, y no se compartirán con terceros sin consentimiento.\n\n")

                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append("4. Responsabilidad del usuario\n")
                            }
                            append("Es responsabilidad del usuario mantener su información actualizada y no compartir sus credenciales de acceso. Cualquier uso indebido de su cuenta será responsabilidad del titular registrado.\n\n")

                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append("5. Modificaciones y suspensión\n")
                            }
                            append("El municipio y los administradores del programa se reservan el derecho de modificar, suspender o cancelar el acceso a la app en caso de incumplimiento de estos términos o por mantenimiento del sistema.\n\n")

                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append("6. Soporte y contacto\n")
                            }
                            append("Para dudas, problemas técnicos o reportes de mal funcionamiento, los usuarios pueden contactar a soporte a través del panel de ayuda disponible en la app o acudir directamente a las oficinas de la Dirección de Juventud.")
                        },
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.bodyLarge, // Un poco más grande para mejorar legibilidad
                        textAlign = TextAlign.Justify // Justificar el texto se ve más profesional
                    )
                }
            }
        }
    }
}
