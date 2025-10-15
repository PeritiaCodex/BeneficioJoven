package mx.itesm.beneficiojoven.view.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import mx.itesm.beneficiojoven.vm.CouponListVM
import mx.itesm.beneficiojoven.model.Coupon
import mx.itesm.beneficiojoven.view.ui.rememberAppImageLoader

/**
 * Pantalla que muestra los **cupones disponibles** para un comercio específico.
 *
 * Consume el estado expuesto por [CouponListVM], filtra por [merchantName] y
 * despliega tarjetas con la información del cupón, incluyendo una sección
 * expandible con **QR** y código.
 *
 * También muestra barra de filtros superpuesta y un botón “Atrás”.
 *
 * @param merchantName Nombre del comercio por el que se filtrará la lista.
 * @param vm ViewModel que provee la colección completa de cupones y estados de carga/error.
 * @param onBack Acción a ejecutar cuando el usuario decide volver.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CouponScreen(
    merchantName: String,
    vm: CouponListVM,
    onBack: () -> Unit = {}
) {
    val loading by vm.loading.collectAsState()
    val error by vm.error.collectAsState()
    val all by vm.coupons.collectAsState()

    val coupons = remember(all, merchantName) { all.filter { it.merchant.name == merchantName } }
    val merchantInfo = coupons.firstOrNull()

    var isFilterExpanded by remember { mutableStateOf(false) }
    var expandedCouponId by remember { mutableStateOf<String?>(null) }

    GradientScreenLayout {
        Column(Modifier.fillMaxSize()) {
            // título dinámico
            Spacer(Modifier.height(14.dp))
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Atrás",
                        tint = Color.White
                    )
                }
                Text(
                    text = "Cupones Disponibles",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            if (merchantInfo != null) {
                Spacer(Modifier.height(12.dp))
                MerchantHeaderCard(
                    name = merchantInfo.merchant.name,
                    logoUrl = merchantInfo.merchant.logoUrl
                )
                Spacer(Modifier.height(16.dp))
            }

            Box(Modifier.weight(1f)) {
                when {
                    loading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                    error != null -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Error: $error", color = Color.White)
                            Spacer(Modifier.height(8.dp))
                            Button(onClick = vm::refresh) { Text("Reintentar") }
                        }
                    }
                    coupons.isEmpty() -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No hay cupones para $merchantName", color = Color.White)
                    }
                    else -> LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(24.dp),
                        contentPadding = PaddingValues(bottom = 80.dp) // sin top padding
                    ) {
                        items(coupons, key = { it.id }) { c ->
                            CouponCard(
                                coupon = c,
                                isExpanded = (expandedCouponId == c.id),
                                onClick = {
                                    expandedCouponId = if (expandedCouponId == c.id) null else c.id
                                }
                            )
                        }
                    }
                }
            }

            BottomMenu()
        }
    }
}

/**
 * Cabecera fija del comercio actual.
 *
 * Muestra el **logo** (si existe; en caso contrario un placeholder) y el nombre
 * del comercio. Incluye un **switch** para simular suscripción a notificaciones.
 *
 * @param name Nombre del comercio.
 * @param logoUrl URL del logo (puede ser nulo).
 */
@Composable
fun MerchantHeaderCard(name: String, logoUrl: String?) {
    val imageLoader = rememberAppImageLoader()

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
                model = logoUrl ?: "https://picsum.photos/seed/${name.hashCode()}/120/120",
                imageLoader = imageLoader,
                contentDescription = "Logo de $name",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(50.dp)
                    .background(Color.White, CircleShape)
                    .padding(4.dp)
            )
            Spacer(Modifier.width(16.dp))
            Column(Modifier.weight(1f)) {
                Text("Cupones de:", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                Text(name, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(imageVector = Icons.Default.Notifications, contentDescription = "Suscripción", tint = Color.White)
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

/**
 * Tarjeta visual de un **cupón** con forma de boleto (Ticket).
 *
 * La parte superior muestra logo e información del cupón; al expandirla,
 * se presentan el **QR** y el texto del código.
 *
 * @param coupon Modelo de dominio del cupón.
 * @param isExpanded Controla si se muestra la sección expandida.
 * @param onClick Acción al pulsar la tarjeta (usar para expandir/colapsar).
 */
@Composable
fun CouponCard(coupon: Coupon, isExpanded: Boolean, onClick: () -> Unit) {
    val imageLoader = rememberAppImageLoader()

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
                    // Izquierda: Logo del comercio
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(0.35f)
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        AsyncImage(
                            model = coupon.merchant.logoUrl ?: "https://picsum.photos/seed/${coupon.merchant.name.hashCode()}/120/120",
                            imageLoader = imageLoader,
                            contentDescription = "Logo de ${coupon.merchant.name}",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.size(80.dp)
                        )
                    }

                    DottedVerticalDivider()

                    // Derecha: Detalles
                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(0.65f)
                            .padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 12.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = coupon.title,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = Color.Black,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(text = coupon.merchant.name, fontSize = 12.sp, color = Color.Gray)
                        }
                        Text(
                            text = "Válido hasta: ${coupon.validUntil ?: "N/D"}",
                            fontSize = 11.sp,
                            color = Color.DarkGray
                        )
                    }
                }

                // Botón Guardar (favoritos)
                IconButton(
                    onClick = { /* TODO: guardar cupón */ },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(4.dp)
                ) {
                    Icon(imageVector = Icons.Default.BookmarkBorder, contentDescription = "Guardar Cupón", tint = Color.Gray)
                }
            }

            // Sección expandible: QR y código
            AnimatedVisibility(visible = isExpanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    DottedHorizontalDivider()
                    Spacer(Modifier.height(16.dp))
                    AsyncImage(
                        model = coupon.qrUrl ?: "https://api.qrserver.com/v1/create-qr-code/?data=${coupon.id}&size=220x220&margin=0",
                        contentDescription = "Código QR para ${coupon.title}",
                        modifier = Modifier.size(150.dp)
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "Código: ${coupon.discountText}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                }
            }
        }
    }
}

/**
 * Divisor vertical punteado usado dentro de la tarjeta tipo ticket.
 */
@Composable
fun DottedVerticalDivider() {
    Canvas(modifier = Modifier
        .fillMaxHeight()
        .width(1.dp)) {
        val pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
        drawLine(color = Color.LightGray, start = Offset(0f, 0f), end = Offset(0f, size.height), strokeWidth = 2f, pathEffect = pathEffect)
    }
}

/**
 * Divisor horizontal punteado usado en la sección expandible del cupón.
 */
@Composable
fun DottedHorizontalDivider() {
    Canvas(modifier = Modifier
        .fillMaxWidth()
        .height(1.dp)) {
        val pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
        drawLine(color = Color.LightGray, start = Offset(0f, 0f), end = Offset(size.width, 0f), strokeWidth = 2f, pathEffect = pathEffect)
    }
}

/**
 * Forma personalizada de **boleto (ticket)** con esquinas redondeadas
 * y dos muescas circulares laterales.
 *
 * @param cornerRadius Radio de las esquinas.
 * @param notchRadius Radio de cada muesca lateral.
 */
class TicketShape(private val cornerRadius: Float, private val notchRadius: Float) : Shape {
    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
        val path = Path().apply {
            fillType = PathFillType.EvenOdd
            addRoundRect(RoundRect(Rect(Offset.Zero, size), CornerRadius(cornerRadius)))
            val leftNotchRect = Rect(
                left = -notchRadius,
                top = size.height / 2 - notchRadius,
                right = notchRadius,
                bottom = size.height / 2 + notchRadius
            )
            addOval(leftNotchRect)
            val rightNotchRect = Rect(
                left = size.width - notchRadius,
                top = size.height / 2 - notchRadius,
                right = size.width + notchRadius,
                bottom = size.height / 2 + notchRadius
            )
            addOval(rightNotchRect)
        }
        return Outline.Generic(path)
    }
}
