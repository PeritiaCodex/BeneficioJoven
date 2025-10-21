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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import mx.itesm.beneficiojoven.model.Coupon
import mx.itesm.beneficiojoven.view.ui.rememberAppImageLoader
import mx.itesm.beneficiojoven.vm.CouponListVM
import mx.itesm.beneficiojoven.vm.FavoritesVM

/**
 * Pantalla que muestra los **cupones disponibles** para un comercio específico.
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
    favoritesVM: FavoritesVM = viewModel(),
    onBack: () -> Unit = {},
    onOpenFavorites: () -> Unit = {},
    onOpenProfile: () -> Unit = {}
) {
    val loading by vm.loading.collectAsState()
    val error by vm.error.collectAsState()
    val all by vm.coupons.collectAsState()

    val coupons = remember(all, merchantName) { all.filter { it.merchant.name == merchantName } }
    val merchantInfo = coupons.firstOrNull()

    var expandedCouponId by remember { mutableStateOf<String?>(null) }

    GradientScreenLayout {
        Column(Modifier.fillMaxSize()) {
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
                        tint = MaterialTheme.colorScheme.outlineVariant
                    )
                }
                Text(
                    text = "Cupones Disponibles",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.outlineVariant
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
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
                    }
                    error != null -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Error: $error", color = MaterialTheme.colorScheme.onError)
                            Spacer(Modifier.height(8.dp))
                            Button(onClick = vm::refresh) { Text("Reintentar") }
                        }
                    }
                    coupons.isEmpty() -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No hay cupones para $merchantName", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    else -> LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(24.dp),
                        contentPadding = PaddingValues(bottom = 80.dp)
                    ) {
                        items(coupons, key = { it.id }) { c ->
                            val isFavorite by favoritesVM.repo().isFavoriteFlow(c.id).collectAsState(initial = false)

                            CouponCard(
                                coupon = c,
                                isExpanded = (expandedCouponId == c.id),
                                isFavorite = isFavorite,
                                onClick = {
                                    expandedCouponId = if (expandedCouponId == c.id) null else c.id
                                },
                                onToggleFavorite = {
                                    favoritesVM.toggle(c, !isFavorite)
                                }
                            )
                        }
                    }
                }
            }

            BottomMenu(
                onOpenFavorites = onOpenFavorites,
                onOpenProfile = onOpenProfile
            )
        }
    }
}

/**
 * Cabecera fija del comercio actual.
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
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f)),
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
                    .background(MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.25f), CircleShape)
                    .padding(4.dp)
            )
            Spacer(Modifier.width(16.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    "Cupones de:",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                )
                Text(
                    name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.outlineVariant
                )
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Suscripción",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Switch(
                    checked = isSubscribed,
                    onCheckedChange = { isSubscribed = it },
                    colors = SwitchDefaults.colors(
                        checkedTrackColor = MaterialTheme.colorScheme.onPrimary,
                        checkedThumbColor = MaterialTheme.colorScheme.onSecondary
                    )
                )
            }
        }
    }
}

/**
 * Tarjeta visual de un **cupón** con forma de boleto (Ticket).
 *
 * @param coupon Modelo de dominio del cupón.
 * @param isExpanded Controla si se muestra la sección expandida.
 * @param onClick Acción al pulsar la tarjeta (usar para expandir/colapsar).
 */
@Composable
fun CouponCard(
    coupon: Coupon,
    isExpanded: Boolean,
    isFavorite: Boolean,
    onClick: () -> Unit,
    onToggleFavorite: () -> Unit
) {
    val imageLoader = rememberAppImageLoader()

    Card(
        shape = TicketShape(cornerRadius = 24f, notchRadius = 40f),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.25f))
    ) {
        Column {
            Box {
                Row(
                    modifier = Modifier.height(140.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
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
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.outlineVariant,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = coupon.merchant.name,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Text(
                            text = "Válido hasta: ${coupon.validUntil ?: "N/D"}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                IconButton(
                    onClick = onToggleFavorite,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(4.dp)
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = if (isFavorite) "Quitar de Favoritos" else "Guardar Cupón",
                        tint = if (isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

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
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurface
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
    val dividerColor = MaterialTheme.colorScheme.outlineVariant
    Canvas(modifier = Modifier
        .fillMaxHeight()
        .width(1.dp)) {
        val pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
        drawLine(
            color = dividerColor,
            start = Offset(0f, 0f),
            end = Offset(0f, size.height),
            strokeWidth = 2f,
            pathEffect = pathEffect
        )
    }
}

/**
 * Divisor horizontal punteado usado en la sección expandible del cupón.
 */
@Composable
fun DottedHorizontalDivider() {
    val dividerColor = MaterialTheme.colorScheme.outlineVariant
    Canvas(modifier = Modifier
        .fillMaxWidth()
        .height(1.dp)) {
        val pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
        drawLine(
            color = dividerColor,
            start = Offset(0f, 0f),
            end = Offset(size.width, 0f),
            strokeWidth = 2f,
            pathEffect = pathEffect
        )
    }
}

/**
 * Forma personalizada de **boleto (ticket)**.
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
