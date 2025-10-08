package mx.diz.beneficiojovenui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MyCouponsScreen(onCouponClick: () -> Unit = {}) {
    val coupons = remember {
        listOf(
            "Six Flags - 2x1 Lunes a Miércoles",
            "Cupón A",
            "Cupón B",
            "Cupón C",
            "Cupón X",
            "Cupón Y",
            "Cupón Z"
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF3B82F6), Color(0xFF6D28D9))
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Título principal
            Text(
                text = "Mis Cupones",
                color = Color.White,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Barra de filtros (placeholder)
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.15f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FilterChip(label = "Entretenimiento")
                    FilterChip(label = "Six Flags")
                    FilterChip(label = "2 meses")
                }
            }

            // Lista de cupones desplazable
            LazyColumn(
                modifier = Modifier
                    .weight(1f) // ocupa el espacio disponible
                    .padding(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 80.dp) // evita que el menú tape el último cupón
            ) {
                items(coupons) { coupon ->
                    CouponCard(
                        title = coupon,
                        onClick = { onCouponClick() }
                    )
                }
            }


            // Menú inferior
            BottomMenu()
        }
    }
}

@Composable
fun FilterChip(label: String) {
    Surface(
        color = Color.White.copy(alpha = 0.2f),
        shape = MaterialTheme.shapes.small,
        modifier = Modifier.padding(horizontal = 4.dp)
    ) {
        Text(
            text = label,
            color = Color.White,
            fontSize = 14.sp,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Composable
fun CouponCard(title: String, onClick: () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f)),
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = title,
                color = Color.Black,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun BottomMenu() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .background(Color.White.copy(alpha = 0.15f))
            .padding(horizontal = 24.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { /* Tarjeta digital */ }) {
            Icon(
                imageVector = Icons.Default.CreditCard,
                contentDescription = "Tarjeta",
                tint = Color.White,
                modifier = Modifier.size(32.dp)
            )
        }
        IconButton(onClick = { /* Cupones */ }) {
            Icon(
                imageVector = Icons.Default.LocalOffer,
                contentDescription = "Cupones",
                tint = Color.White,
                modifier = Modifier.size(32.dp)
            )
        }
        IconButton(onClick = { /* Usuario */ }) {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Usuario",
                tint = Color.White,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}
