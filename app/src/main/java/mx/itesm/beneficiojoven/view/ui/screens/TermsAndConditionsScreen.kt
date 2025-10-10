package mx.diz.beneficiojoven.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import mx.diz.beneficiojovenui.GradientScreenLayout

@Composable
fun TermsAndConditionsScreen(onCloseClicked: () -> Unit = {}) {
    GradientScreenLayout {
        Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
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
                modifier = Modifier.weight(1f)
            )

            IconButton(onClick = { onCloseClicked() }) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Cerrar",
                    tint = Color.White
                )
            }
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
