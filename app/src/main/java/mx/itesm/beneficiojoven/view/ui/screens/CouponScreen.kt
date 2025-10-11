package mx.itesm.beneficiojoven.view.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage

// Clase de datos para Cupones
data class Coupon(
    val coupon_id: String,
    val code: String,
    val title: String,
    val description: String,
    val discount_type: String, // "percentage", "fixed_amount"
    val valid_from: String,
    val valid_until: String,
    val usage_limit: Int,
    val qr_code_url: String,
    val merchant_name: String,
    val merchant_logo: String,
    val merchant_type: String
)

// Pantalla de Cupones (Por negocio)
@OptIn(ExperimentalFoundationApi::class) // Necesario para stickyHeader
@Composable
fun CouponScreen() {
    val coupons = remember {
        listOf(
            Coupon("c1",
                "SAVE20",
                "20% de descuento en Entradas de Cine",
                "Aplica en toda la tienda",
                "percentage",
                "2024-01-01", "2024-12-31",
                100,
                "https://api.qrserver.com/v1/create-qr-code/?data=Insertar+aqu%C3%AD+el+c%C3%B3digo+qr....%0A%0A(Tambi%C3%A9n+puto+el+que+lo+lea)&size=220x220&margin=0",
                "Cinépolis",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRWBcBPS21etAxJKbyL-XrOrUma-3FcWnrH5Q&s",
                "Entretenimiento"),

            Coupon("c2",
                "PALOMASJUMBO50",
                "$50 Menos en Palomitas Jumbo",
                "Válido en la compra de palomitas jumbo de cualquier especialidad.",
                "fixed_amount",
                "2024-05-10", "2024-11-10",
                200,
                "https://api.qrserver.com/v1/create-qr-code/?data=Insertar+aqu%C3%AD+el+c%C3%B3digo+qr....%0A%0A(Tambi%C3%A9n+puto+el+que+lo+lea)&size=220x220&margin=0",
                "Cinépolis",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRWBcBPS21etAxJKbyL-XrOrUma-3FcWnrH5Q&s",
                "Entretenimiento"),

            Coupon("c3",
                "CINE2X1",
                "2x1 en Entradas de Cine",
                "Válido de Lunes a Jueves en salas tradicionales.",
                "special",
                "2024-06-01", "2024-09-30",
                50,
                "https://api.qrserver.com/v1/create-qr-code/?data=Insertar+aqu%C3%AD+el+c%C3%B3digo+qr....%0A%0A(Tambi%C3%A9n+puto+el+que+lo+lea)&size=220x220&margin=0",
                "Cinépolis",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRWBcBPS21etAxJKbyL-XrOrUma-3FcWnrH5Q&s",
                "Entretenimiento")
        )
    }
    // Suponiendo que todos los cupones son del mismo negocio, tomamos la info del primero.
    val merchantInfo = coupons.firstOrNull()

    var isFilterExpanded by remember { mutableStateOf(false) }

    GradientScreenLayout {
        Column(modifier = Modifier.fillMaxSize()) {
            Header(title = "Cupones Disponibles") // Título dinámico
            Spacer(modifier = Modifier.height(16.dp))

            var expandedCouponId by remember { mutableStateOf<String?>(null) }

            Box(modifier = Modifier.weight(1f)) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .zIndex(0f),
                    verticalArrangement = Arrangement.spacedBy(24.dp), // Aumentamos el espacio
                    contentPadding = PaddingValues(top = 80.dp, bottom = 80.dp)
                ) {

                    // -- NUEVA SECCIÓN STICKY PARA EL HEADER DEL NEGOCIO --
                    if (merchantInfo != null) {
                        stickyHeader {
                            MerchantHeaderCard(
                                name = merchantInfo.merchant_name,
                                logoUrl = merchantInfo.merchant_logo
                            )
                            Spacer(modifier = Modifier.height(24.dp)) // Espacio después del header
                        }
                    }

                    // --- LISTA DE CUPONES ---
                    items(coupons, key = { it.coupon_id }) { coupon ->
                        CouponCard(
                            coupon = coupon,
                            isExpanded = expandedCouponId == coupon.coupon_id,
                            onClick = {
                                expandedCouponId = if (expandedCouponId == coupon.coupon_id) null else coupon.coupon_id
                            }
                        )
                        // El botón de suscripción individual se ha eliminado
                    }
                }

                // La barra de filtros sigue superponiéndose en la parte superior
                Column(modifier = Modifier.zIndex(1f)) {
                    FilterBar(
                        isExpanded = isFilterExpanded,
                        onToggle = { isFilterExpanded = !isFilterExpanded }
                    )
                    ExpandedFiltersPanel(visible = isFilterExpanded)
                }
            }

            BottomMenu()
        }
    }
}

// --- NUEVO COMPONENTE: Tarjeta del Negocio con Botón de Suscripción ---
@Composable
fun MerchantHeaderCard(name: String, logoUrl: String) {
    var isSubscribed by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.2f)),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = logoUrl,
                contentDescription = "Logo de $name",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(50.dp)
                    .background(Color.White, CircleShape)
                    .padding(4.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Cupones de:", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                Text(text = name, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Suscripción",
                    tint = Color.White
                )
                Switch(
                    checked = isSubscribed,
                    onCheckedChange = { isSubscribed = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = Color(0xFF3B82F6)
                    )
                )
            }
        }
    }
}


// --- COMPONENTES ACTUALIZADOS Y EXISTENTES ---

// CouponCard ya no necesita el botón de suscripción debajo
@Composable
fun CouponCard(coupon: Coupon, isExpanded: Boolean, onClick: () -> Unit) {
    Card(
        shape = TicketShape(cornerRadius = 24f, notchRadius = 40f),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.95f))
    ) {
        Column {
            Box {
                Row(
                    modifier = Modifier.height(140.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Parte Izquierda: Logo
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(0.35f)
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        AsyncImage(model = coupon.merchant_logo, contentDescription = "Logo de ${coupon.merchant_name}", contentScale = ContentScale.Fit, modifier = Modifier.size(80.dp))
                    }

                    DottedVerticalDivider()

                    // Parte Derecha: Detalles
                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(0.65f)
                            .padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 12.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(text = coupon.title, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.Black, maxLines = 2, overflow = TextOverflow.Ellipsis)
                            Text(text = coupon.merchant_name, fontSize = 12.sp, color = Color.Gray)
                        }
                        Text(text = "Válido hasta: ${coupon.valid_until}", fontSize = 11.sp, color = Color.DarkGray)
                    }
                }

                // Botón de Guardar
                IconButton(
                    onClick = { /* TODO: Lógica para guardar cupón */ },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(4.dp)
                ) {
                    Icon(imageVector = Icons.Default.BookmarkBorder, contentDescription = "Guardar Cupón", tint = Color.Gray)
                }
            }

            // Sección expandible con Código QR
            AnimatedVisibility(visible = isExpanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    DottedHorizontalDivider()
                    Spacer(modifier = Modifier.height(16.dp))
                    AsyncImage(
                        model = coupon.qr_code_url,
                        contentDescription = "Código QR para ${coupon.title}",
                        modifier = Modifier.size(150.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Código: ${coupon.code}", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.Black)
                }
            }
        }
    }
}

// El resto de los componentes (Header, DottedDividers, TicketShape, etc.) permanecen igual
// ... (pega aquí el resto de tus componentes auxiliares si están en el mismo archivo) ...

@Composable
fun Header(title: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun DottedVerticalDivider() {
    Canvas(modifier = Modifier
        .fillMaxHeight()
        .width(1.dp)) {
        val pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
        drawLine(color = Color.LightGray, start = Offset(0f, 0f), end = Offset(0f, size.height), strokeWidth = 2f, pathEffect = pathEffect)
    }
}

@Composable
fun DottedHorizontalDivider() {
    Canvas(modifier = Modifier
        .fillMaxWidth()
        .height(1.dp)) {
        val pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
        drawLine(color = Color.LightGray, start = Offset(0f, 0f), end = Offset(size.width, 0f), strokeWidth = 2f, pathEffect = pathEffect)
    }
}

class TicketShape(private val cornerRadius: Float, private val notchRadius: Float) : Shape {
    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
        val path = Path().apply {
            fillType = PathFillType.EvenOdd
            addRoundRect(RoundRect(Rect(Offset.Zero, size),
                CornerRadius(cornerRadius)
            ))
            val leftNotchRect = Rect(left = -notchRadius, top = size.height / 2 - notchRadius, right = notchRadius, bottom = size.height / 2 + notchRadius)
            addOval(leftNotchRect)
            val rightNotchRect = Rect(left = size.width - notchRadius, top = size.height / 2 - notchRadius, right = size.width + notchRadius, bottom = size.height / 2 + notchRadius)
            addOval(rightNotchRect)
        }
        return Outline.Generic(path)
    }
}