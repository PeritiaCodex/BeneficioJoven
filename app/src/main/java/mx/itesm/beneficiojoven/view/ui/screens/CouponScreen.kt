package mx.itesm.beneficiojoven.view.ui.screens

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import mx.itesm.beneficiojoven.model.Coupon
import mx.itesm.beneficiojoven.utils.LocationManager
import mx.itesm.beneficiojoven.view.ui.rememberAppImageLoader
import mx.itesm.beneficiojoven.vm.CouponListVM
import mx.itesm.beneficiojoven.vm.FavoritesVM
import mx.itesm.beneficiojoven.vm.SubscriptionViewModel

/**
 * Pantalla que muestra los **cupones disponibles** para un comercio específico.
 *
 * Despliega una lista de cupones filtrada por el `merchantName` y gestiona la interacción
 * del usuario, como expandir cupones, marcarlos como favoritos y suscribirse al comercio.
 * También integra la funcionalidad de geolocalización para buscar el comercio en un mapa.
 *
 * @param merchantName Nombre del comercio por el que se filtrará la lista.
 * @param vm ViewModel que provee la colección completa de cupones y estados de carga/error.
 * @param favoritesVM ViewModel para gestionar los cupones favoritos.
 * @param onBack Acción a ejecutar cuando el usuario decide volver.
 * @param onOpenFavorites Acción para navegar a la pantalla de favoritos.
 * @param onOpenProfile Acción para navegar al perfil del usuario.
 * @param subscriptionVM ViewModel para gestionar la suscripción al comercio.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CouponScreen(
    merchantName: String,
    vm: CouponListVM,
    favoritesVM: FavoritesVM = viewModel(),
    onBack: () -> Unit = {},
    onOpenFavorites: () -> Unit = {},
    onOpenProfile: () -> Unit = {},
    subscriptionVM: SubscriptionViewModel = viewModel()
) {
    val loading by vm.loading.collectAsState()
    val error by vm.error.collectAsState()
    val all by vm.coupons.collectAsState()
    val merchantIdMap by vm.merchantIdMap.collectAsState() // Directorio de IDs

    val coupons = remember(all, merchantName) { all.filter { it.merchant.name == merchantName } }
    val merchantInfo = coupons.firstOrNull()

    var expandedCouponId by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    val locationManager = remember { LocationManager(context) }
    val currentLocation by locationManager.currentLocation.collectAsState()

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                locationManager.getLastKnownLocation()
            } else {
                Toast.makeText(context, "Permiso de ubicación denegado.", Toast.LENGTH_SHORT).show()
            }
        }
    )
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
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(32.dp)
                    )
                }
                Text(
                    text = "Cupones Disponibles",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                IconButton(
                    onClick = {
                        if (locationManager.hasLocationPermission()) {
                            locationManager.getLastKnownLocation()
                            val userLocation = currentLocation
                            val query = if (userLocation != null) {
                                "$merchantName cerca de ${userLocation.latitude},${userLocation.longitude}"
                            } else {
                                merchantName
                            }
                            val gmmIntentUri = Uri.parse("geo:0,0?q=${Uri.encode(query)}")
                            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri).apply {
                                setPackage("com.google.android.apps.maps")
                            }
                            context.startActivity(mapIntent)
                        } else {
                            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                        }
                    },
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    Icon(
                        imageVector = Icons.Default.Place,
                        contentDescription = "Buscar en mapa",
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
            }
            if (merchantInfo != null) {
                // AQUÍ ESTÁ EL CAMBIO: Buscamos el ID numérico.
                val numericMerchantId = merchantIdMap[merchantInfo.merchant.name]?.toString()

                if (numericMerchantId != null) {
                    Spacer(Modifier.height(12.dp))
                    MerchantHeaderCard(
                        merchantId = numericMerchantId, // Y se lo pasamos a la tarjeta.
                        name = merchantInfo.merchant.name,
                        logoUrl = merchantInfo.merchant.logoUrl,
                        subscriptionVM = subscriptionVM
                    )
                    Spacer(Modifier.height(16.dp))
                }
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
 * Cabecera que muestra la información del comercio y permite la suscripción a notificaciones.
 *
 * @param merchantId ID del comercio.
 * @param name Nombre del comercio.
 * @param logoUrl URL del logo del comercio.
 * @param subscriptionVM ViewModel para gestionar la lógica de suscripción.
 */
@Composable
fun MerchantHeaderCard(
    merchantId: String,
    name: String,
    logoUrl: String?,
    subscriptionVM: SubscriptionViewModel
) {
    val imageLoader = rememberAppImageLoader()
    val isSubscribed by subscriptionVM.isSubscribed.collectAsState()
    val isLoading by subscriptionVM.loading.collectAsState()
    val error by subscriptionVM.error.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(merchantId) {
        subscriptionVM.checkInitialSubscription(merchantId)
    }

    LaunchedEffect(error) {
        error?.let {
            Toast.makeText(context, "Error de suscripción: $it", Toast.LENGTH_SHORT).show()
            subscriptionVM.clearError()
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.2f)),
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
                    .background(MaterialTheme.colorScheme.onTertiary.copy(alpha = 0.25f), CircleShape)
                    .padding(4.dp)
            )
            Spacer(Modifier.width(16.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notificaciones",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = "Notificaciones",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            if (isSubscribed == null || isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp))
            } else {
                Switch(
                    checked = isSubscribed == true,
                    onCheckedChange = { subscriptionVM.toggleSubscription(merchantId) },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = MaterialTheme.colorScheme.primary,
                        checkedTrackColor = MaterialTheme.colorScheme.primaryContainer,
                        uncheckedThumbColor = MaterialTheme.colorScheme.outline,
                        uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant,
                    )
                )
            }
        }
    }
}

/**
 * Tarjeta visual de un **cupón** con forma de boleto (Ticket).
 *
 * @param coupon Modelo de dominio del cupón a mostrar.
 * @param isExpanded Controla si se muestra la sección expandida con el código QR.
 * @param isFavorite Indica si el cupón está marcado como favorito por el usuario.
 * @param onClick Acción que se ejecuta al pulsar la tarjeta para expandir/colapsar.
 * @param onToggleFavorite Callback para cambiar el estado de favorito del cupón.
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
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 1f))
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
                                color = MaterialTheme.colorScheme.onSurface,
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
                            color = MaterialTheme.colorScheme.secondary
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
                        imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = if (isFavorite) "Quitar de Favoritos" else "Guardar Cupón",
                        tint = if (isFavorite) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSurfaceVariant
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
 * Divisor vertical punteado, usado para separar visualmente secciones en la `CouponCard`.
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
 * Divisor horizontal punteado, usado en la sección expandible de la `CouponCard`.
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
 * `Shape` personalizada que crea una forma de boleto con muescas semicirculares en los lados.
 *
 * @property cornerRadius Radio de las esquinas redondeadas del boleto.
 * @property notchRadius Radio de las muescas semicirculares en los lados.
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
