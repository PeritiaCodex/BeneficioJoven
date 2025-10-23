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
import androidx.compose.ui.text.style.TextAlign
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
                        text = """
                            Acá estarían los términos y condiciones...
                            
                            Lorem ipsum dolor sit amet, consectetur adipiscing elit. 
                            Nulla vel odio eu ligula suscipit iaculis. 
                            Curabitur eu nisl nec lorem facilisis faucibus. 
                            Maecenas tempus orci ut lacus interdum, vel lacinia leo fermentum. 
                            Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas.
                            
                            Suspendisse eget turpis et metus volutpat faucibus. 
                            Morbi ultricies purus non justo fermentum, at blandit ex imperdiet. 
                            Quisque at lorem ac velit viverra varius non non ligula.
                            
                            Donec ut libero in mi cursus pulvinar. 
                            Cras in sem vel justo ultrices imperdiet. 
                            Integer luctus nunc vel enim cursus, non vestibulum risus ultricies.
                            
                            Nam fermentum bibendum mi sit amet efficitur. 
                            Aliquam erat volutpat. 
                            Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae.
                            
                            Lorem ipsum dolor sit amet, consectetur adipiscing elit. 
                            Nulla vel odio eu ligula suscipit iaculis. 
                            Curabitur eu nisl nec lorem facilisis faucibus. 
                            Maecenas tempus orci ut lacus interdum, vel lacinia leo fermentum. 
                            Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas.
                            
                            Suspendisse eget turpis et metus volutpat faucibus. 
                            Morbi ultricies purus non justo fermentum, at blandit ex imperdiet. 
                            Quisque at lorem ac velit viverra varius non non ligula.
                            
                            Donec ut libero in mi cursus pulvinar. 
                            Cras in sem vel justo ultrices imperdiet. 
                            Integer luctus nunc vel enim cursus, non vestibulum risus ultricies.
                            
                            Nam fermentum bibendum mi sit amet efficitur. 
                            Aliquam erat volutpat. 
                            Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae.
                        """.trimIndent(),

                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}
