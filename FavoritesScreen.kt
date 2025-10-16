package mx.itesm.beneficiojoven.view.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import mx.itesm.beneficiojoven.model.Coupon // Asegúrate de importar tu modelo de Coupon
import mx.itesm.beneficiojoven.view.ui.rememberAppImageLoader
import mx.itesm.beneficiojoven.vm.CouponListVM

/**
 * Pantalla que muestra la lista de **cupones guardados como favoritos**.
 *
 * Utiliza el mismo ViewModel [CouponListVM] que la pantalla de negocios para
 * obtener la lista completa de cupones y la filtra para mostrar solo los favoritos.
 *
 * @param vm ViewModel que provee el estado de carga y la lista de cupones.
 * @param onOpenCoupon Callback para navegar al detalle de un cupón.
 * @param onToggleFavorite Callback para cambiar el estado de favorito de un cupón.
 */
@Composable
fun FavoritesScreen(
    vm: CouponListVM,
    onOpenCoupon: (couponId: String) -> Unit,
    onToggleFavorite: (couponId: String, isFavorite: Boolean) -> Unit // Para actualizar el estado
) {
    val loading by vm.loading.collectAsState()
    val error by vm.error.collectAsState()
    val allCoupons by vm.coupons.collectAsState()

    // Filtra la lista completa para obtener solo los favoritos.
    // 'remember' recalculará esto solo si 'allCoupons' cambia.
    val favoriteCoupons = remember(allCoupons) {
        allCoupons.filter { it.isFavorite == true } // Asume que Coupon tiene 'isFavorite: Boolean'
    }

    // Carga inicial si no hay datos y no se está cargando.
    LaunchedEffect(Unit) { if (allCoupons.isEmpty() && !loading) vm.refresh() }

    GradientScreenLayout {
        Column(modifier = Modifier.fillMaxSize()) {
            FavoritesHeader()

            Spacer(modifier = Modifier.height(16.dp))

            Box(modifier = Modifier.weight(1f)) {
                when {
                    loading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color.White)
                    }
                    error != null -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Error: $error", color = Color.White)
                            Spacer(Modifier.height(8.dp))
                            Button(onClick = vm::refresh) { Text("Reintentar") }
                        }
                    }
                    favoriteCoupons.isEmpty() -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.FavoriteBorder,
                                contentDescription = "No hay favoritos",
                                tint = Color.White.copy(alpha = 0.7f),
                                modifier = Modifier.size(64.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                "Aún no tienes cupones favoritos",
                                color = Color.White.copy(alpha = 0.9f),
                                fontSize = 18.sp
                            )
                            Text(
                                "¡Explora el catálogo y guarda los que más te gusten!",
                                color = Color.White.copy(alpha = 0.7f)
                            )
                        }
                    }
                    else -> LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 24.dp)
                    ) {
                        items(favoriteCoupons, key = { it.id }) { coupon ->
                            FavoriteCouponCard(
                                coupon = coupon,
                                onClick = { onOpenCoupon(coupon.id) },
                                onToggleFavorite = { coupon.isFavorite?.let { onToggleFavorite(coupon.id, !it) } }
                            )
                        }
                    }
                }
            }

            BottomMenu() // Reutilizamos el menú inferior
        }
    }
}

/**
 * Encabezado simple para la pantalla de favoritos.
 */
@Composable
fun FavoritesHeader() {
    Spacer(Modifier.height(16.dp))
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Favorite,
            contentDescription = "Icono de Favoritos",
            tint = Color.White,
            modifier = Modifier.size(30.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = "Mis Favoritos",
            color = Color.White,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

/**
 * Tarjeta para mostrar un cupón en la lista de favoritos.
 *
 * @param coupon El objeto de datos del cupón a mostrar.
 * @param onClick Acción al pulsar sobre la tarjeta.
 * @param onToggleFavorite Acción al pulsar el icono de favorito.
 */
@Composable
fun FavoriteCouponCard(
    coupon: Coupon,
    onClick: () -> Unit,
    onToggleFavorite: () -> Unit
) {
    val imageLoader = rememberAppImageLoader()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Imagen del comercio
            AsyncImage(
                model = coupon.merchant.logoUrl ?: "https://via.placeholder.com/150",
                imageLoader = imageLoader,
                contentDescription = "Logo de ${coupon.merchant.name}",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp)
                    .padding(end = 12.dp)
            )

            // Columna con la información
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = coupon.merchant.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = coupon.description,
                    fontSize = 14.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Icono para quitar de favoritos
            IconButton(onClick = onToggleFavorite) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Quitar de favoritos",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}