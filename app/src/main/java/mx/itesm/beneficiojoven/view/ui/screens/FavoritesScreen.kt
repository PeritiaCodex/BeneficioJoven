package mx.itesm.beneficiojoven.view.ui.screens

import androidx.benchmark.traceprocessor.Row
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import mx.itesm.beneficiojoven.vm.CouponListVM
import mx.itesm.beneficiojoven.vm.FavoritesVM

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
    favoritesVM: FavoritesVM = viewModel(),
    onOpenCoupon: (couponId: String) -> Unit,
    onBack: () -> Unit = {},
    onOpenFavorites: () -> Unit
) {
    val favoriteCoupons by favoritesVM.repo().observe().collectAsState(initial = emptyList())
    var expandedCouponId by remember { mutableStateOf<String?>(null) }

    GradientScreenLayout { Column(modifier = Modifier.fillMaxSize()) { FavoritesHeader()
        Spacer(modifier = Modifier.height(16.dp))

        Box(modifier = Modifier.weight(1f)) {
            if (favoriteCoupons.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
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
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 24.dp)
                ) {
                    items(favoriteCoupons, key = { it.id }) { coupon ->
                        val isFavorite by favoritesVM.repo().isFavoriteFlow(coupon.id)
                            .collectAsState(initial = true)

                        CouponCard(
                            coupon = coupon,
                            isExpanded = expandedCouponId == coupon.id,
                            isFavorite = isFavorite,
                            onClick = {
                                expandedCouponId = if (expandedCouponId == coupon.id) null else coupon.id
                            },
                            onToggleFavorite = {
                                favoritesVM.toggle(coupon, !isFavorite)
                            }
                        )
                    }
                }
            }
        }

        BottomMenu(onOpenFavorites = onOpenFavorites) // Reutilizamos el menú inferior

    }
}}

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