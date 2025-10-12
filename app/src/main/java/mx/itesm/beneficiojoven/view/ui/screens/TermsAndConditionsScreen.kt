package mx.itesm.beneficiojoven.view.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close

/**
 * Pantalla de **Términos y Condiciones**.
 *
 * Muestra un fondo con degradado, encabezado con botón de cierre y un cuerpo de texto
 * desplazable dentro de una tarjeta translúcida. El contenido es estático (placeholder).
 *
 * @param onBack Acción para cerrar la pantalla y volver a la anterior.
 */
@Composable
fun TermsScreen(
    onBack: () -> Unit = {},
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF3B82F6), Color(0xFF6D28D9)) // azul a morado
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            IconButton(onClick = { onBack() },
                modifier = Modifier.align(Alignment.End)) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Cerrar",
                    tint = Color.White
                )
            }
            // Encabezado con título y botón de cierre
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Términos y Condiciones",
                    color = Color.White,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f).padding(horizontal = 32.dp),
                )


            }

            Spacer(modifier = Modifier.height(16.dp))

            // Contenedor con texto desplazable
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.15f)),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = """
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
                        color = Color.White,
                        fontSize = 16.sp,
                        lineHeight = 22.sp
                    )
                }
            }
        }
    }
}
