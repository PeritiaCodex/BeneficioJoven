package mx.itesm.beneficiojoven.view.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import mx.itesm.beneficiojoven.view.ui.theme.LocalExtendedColors
import mx.itesm.beneficiojoven.vm.CouponListVM
import mx.itesm.beneficiojoven.vm.FavoritesVM

/**
 * Pantalla que muestra la lista de **cupones guardados como favoritos**.
 *
 * Utiliza el mismo ViewModel [CouponListVM] que la pantalla de negocios para
 * obtener la lista completa de cupones y la filtra para mostrar solo los favoritos.
 *
 * @param vm ViewModel que provee el estado de carga y la lista de cupones.
 * @param favoritesVM ViewModel para gestionar los cupones favoritos.
 * @param onOpenCoupon Callback para navegar al detalle de un cupón.
 * @param onBack Acción a ejecutar cuando el usuario decide volver.
 * @param onOpenFavorites Navega a la pantalla de favoritos.
 * @param onOpenProfile Navega a la pantalla de perfil.
 */
@Composable
fun FavoritesScreen(
    vm: CouponListVM = viewModel(),
    favoritesVM: FavoritesVM = viewModel(),
    onOpenCoupon: (couponId: String) -> Unit,
    onBack: () -> Unit = {},
    onOpenFavorites: () -> Unit,
    onOpenProfile: () -> Unit
) {
    // 1. Obtenemos el estado de AMBOS ViewModels
    val loading by vm.loading.collectAsState()
    val error by vm.error.collectAsState()
    val allCoupons by vm.coupons.collectAsState()
    val favoriteLocalCoupons by favoritesVM.favorites.collectAsState()

    // 2. Creamos un Set de IDs de favoritos para una búsqueda eficiente
    val favoriteIds = remember(favoriteLocalCoupons) {
        favoriteLocalCoupons.map { it.id }.toSet()
    }

    // 3. Filtramos la lista COMPLETA de la API para quedarnos solo con los favoritos
    val fullFavoriteCoupons = remember(allCoupons, favoriteIds) {
        allCoupons.filter { it.id in favoriteIds }
    }

    var expandedCouponId by remember { mutableStateOf<String?>(null) }

    GradientScreenLayout {
        Column(modifier = Modifier.fillMaxSize()) {
            FavoritesHeader(onBack)
            Spacer(modifier = Modifier.height(16.dp))

            Box(modifier = Modifier.weight(1f)) {
                // 4. Usamos el mismo manejo de estados que en CouponScreen
                when {
                    loading && allCoupons.isEmpty() -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                    error != null -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Error: $error", color = MaterialTheme.colorScheme.error)
                            Spacer(Modifier.height(8.dp))
                            Button(onClick = vm::refresh) { Text("Reintentar") }
                        }
                    }
                    fullFavoriteCoupons.isEmpty() -> {
                        // Mensaje de "No hay favoritos"
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = Icons.Default.FavoriteBorder,
                                    contentDescription = "No hay favoritos",
                                    tint = MaterialTheme.colorScheme.surfaceTint.copy(alpha = 0.7f),
                                    modifier = Modifier.size(64.dp)
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    "Aún no tienes cupones favoritos",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.9f),
                                    fontSize = 18.sp
                                )
                                Text(
                                    "¡Explora el catálogo y guarda los que más te gusten!",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                                )
                            }
                        }
                    }
                    else -> {
                        // 5. Mostramos la lista con los datos completos
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(24.dp),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp)
                        ) {
                            items(fullFavoriteCoupons, key = { it.id }) { coupon ->
                                val isFavorite by favoritesVM.repo().isFavoriteFlow(coupon.id)
                                    .collectAsState(initial = true) // Es favorito por definición

                                CouponCard( // <-- REUTILIZAMOS LA MISMA TARJETA
                                    coupon = coupon, // Le pasamos el cupón COMPLETO
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
            }
            // Reutilizamos el menú inferior
            BottomMenu(onOpenFavorites = onOpenFavorites,
                onOpenProfile = onOpenProfile)
        }
    }
}

/**
 * Encabezado simple para la pantalla de favoritos.
 * @param onBack Acción a ejecutar cuando el usuario decide volver.
 */
@Composable fun FavoritesHeader(
    onBack: () -> Unit
) {
    val diagonalGradient = LocalExtendedColors.current.diagonalGradientBrush


    Spacer(Modifier.height(16.dp))
    Box(
        modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center
    ) {
        IconButton( onClick = onBack,
            modifier = Modifier
                .align(Alignment.CenterStart)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Atrás",
                tint = MaterialTheme.colorScheme.secondary )
        }
        Icon(
            imageVector = Icons.Default.Favorite,
            contentDescription = "Icono de Favoritos",
            tint = MaterialTheme.colorScheme.secondary.copy(alpha = 0.75f),
            modifier = Modifier.size(30.dp).align(Alignment.CenterEnd)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = "Mis Favoritos",
            style = MaterialTheme.typography.headlineSmall.copy(
                brush = diagonalGradient
            ),
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
