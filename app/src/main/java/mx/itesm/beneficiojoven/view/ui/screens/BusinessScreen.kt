package mx.itesm.beneficiojoven.view.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import androidx.compose.material.icons.filled.CreditCard
import mx.itesm.beneficiojoven.view.ui.components.LiquidGlassCard
import mx.itesm.beneficiojoven.view.ui.components.LocalBackdropBrush
import mx.itesm.beneficiojoven.view.ui.rememberAppImageLoader
import mx.itesm.beneficiojoven.vm.CouponListVM

/**
 * Modelo visual de un **negocio** para la grilla/lista de comercios.
 *
 * @property title Nombre visible del comercio.
 * @property imageUrl URL de imagen/logo del comercio (puede ser SVG/PNG).
 * @property description Texto descriptivo breve.
 * @property type Tipo o categoría del comercio.
 */
data class Business(
    val title: String,
    val imageUrl: String,
    val description: String,
    val type: String
)

/**
 * Pantalla que muestra el **catálogo de negocios** agrupado a partir de la lista
 * de cupones expuesta por [CouponListVM]. Incluye barra de filtros y menú inferior.
 *
 * @param vm ViewModel que provee estados de carga, error y la colección de cupones.
 * @param onOpenMerchant Callback para navegar al listado de cupones de un comercio.
 */
@Composable
fun BusinessesScreen(
    vm: CouponListVM,
    onOpenMerchant: (merchantName: String) -> Unit,
    onOpenFavorites: () -> Unit,
    onOpenProfile: () -> Unit
) {
    val loading by vm.loading.collectAsState()
    val error by vm.error.collectAsState()
    val allCoupons by vm.coupons.collectAsState()

    // Carga inicial si no hay datos y no se está cargando.
    LaunchedEffect(Unit) { if (allCoupons.isEmpty() && !loading) vm.refresh() }

    // Agregación: cupones → negocios (primer cupón aporta logo/desc)
    val businesses: List<Business> = remember(allCoupons) {
        allCoupons
            .groupBy { it.merchant.name }
            .map { (name, list) ->
                val first = list.first()
                Business(
                    title = name,
                    imageUrl = first.merchant.logoUrl ?: "https://picsum.photos/seed/${name.hashCode()}/200/200",
                    description = first.description.ifBlank { "Explora ${list.size} cupón(es) disponibles." },
                    type = first.merchant.type
                )
            }
            .sortedBy { it.title.lowercase() }
    }

    var isFilterExpanded by remember { mutableStateOf(false) }

    GradientScreenLayout {
        Column(modifier = Modifier.fillMaxSize()) {
            Header()

            Spacer(modifier = Modifier.height(16.dp))

            Box(modifier = Modifier.weight(1f)) {
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
                    else -> LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .zIndex(0f),
                        verticalArrangement = Arrangement.spacedBy(24.dp),
                        contentPadding = PaddingValues(top = 80.dp, bottom = 80.dp)
                    ) {
                        items(businesses) { b ->
                            BusinessCard(
                                title = b.title,
                                imageUrl = b.imageUrl,
                                description = b.description,
                                type = b.type,
                                onClick = { onOpenMerchant(b.title) }
                            )
                        }
                    }
                }

                // Contenedor de filtros sobrepuesto
                Column(modifier = Modifier.zIndex(1f)) {
                    FilterBar(
                        isExpanded = isFilterExpanded,
                        onToggle = { isFilterExpanded = !isFilterExpanded }
                    )
                    ExpandedFiltersPanel(visible = isFilterExpanded)
                }
            }

            BottomMenu(onOpenFavorites = onOpenFavorites,
                onOpenProfile = onOpenProfile)
        }
    }
}

/**
 * Encabezado de la pantalla de negocios con título y acceso a **favoritos**.
 */
@Composable
fun Header() {
    Spacer(Modifier.height(16.dp))
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Catálogo de Negocios",
            color = Color.White,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

/**
 * Barra compacta de **filtros** con opción para expandir panel avanzado.
 *
 * @param isExpanded Indica si el panel avanzado está desplegado.
 * @param onToggle Acción para alternar el estado de expansión.
 */
@Composable
fun FilterBar(isExpanded: Boolean, onToggle: () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.Gray.copy(alpha = 0.65f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            FilterChip(label = "Comida", onClick = { /* TODO */ }, modifier = Modifier.weight(1f))
            FilterChip(label = "Salud", onClick = { /* TODO */ }, modifier = Modifier.weight(1f))
            FilterChip(label = "Belleza", onClick = { /* TODO */ }, modifier = Modifier.weight(1f))

            IconToggleButton(checked = isExpanded, onCheckedChange = { onToggle() }) {
                Icon(
                    imageVector = if (isExpanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                    contentDescription = "Expandir filtros",
                    tint = Color.White
                )
            }
        }
    }
}

/**
 * Panel avanzado de **filtros** mostrado/oculto mediante animación.
 *
 * @param visible Controla la visibilidad del panel.
 */
@Composable
fun ExpandedFiltersPanel(visible: Boolean) {
    val allFilters = listOf(
        "Entretenimiento", "Comida", "Salud", "Belleza",
        "Educación", "Moda", "Servicios", "Hogar", "Viajes"
    )

    AnimatedVisibility(visible = visible) {
        Card(
            colors = CardDefaults.cardColors(containerColor = Color.DarkGray.copy(alpha = .75f)),
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium.copy(topStart = CornerSize(0), topEnd = CornerSize(0))
        ) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 120.dp),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(allFilters.size) { index ->
                    FilterChip(label = allFilters[index], onClick = { /* TODO */ })
                }
            }
        }
    }
}

/**
 * Chip de filtro simple.
 *
 * @param label Texto del filtro.
 * @param onClick Acción al pulsar el chip.
 * @param modifier Modificador de composición opcional.
 */
@Composable
fun FilterChip(label: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Surface(
        color = Color.White.copy(alpha = 0.2f),
        shape = MaterialTheme.shapes.small,
        modifier = modifier.clickable { onClick() }
    ) {
        Text(
            text = label,
            color = Color.White,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
        )
    }
}

/**
 * Tarjeta de **negocio** con imagen, título, tipo y descripción.
 *
 * @param title Nombre del negocio.
 * @param imageUrl URL de la imagen/logo a mostrar.
 * @param onClick Acción al pulsar la tarjeta (por ejemplo, abrir cupones del comercio).
 * @param description Descripción corta del negocio.
 * @param type Categoría o tipo del negocio.
 */
@Composable
fun BusinessCard(
    title: String,
    imageUrl: String,
    onClick: () -> Unit,
    description: String,
    type: String
) {
    val imageLoader = rememberAppImageLoader()
    val backdrop = LocalBackdropBrush.current   // del GradientScreenLayout

    LiquidGlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(22.dp),
        cornerRadius = 22.dp,
        blurRadius = 18.dp,     // detalle del fondo más nítido
        tintAlpha = 0.04f,      // velo súper ligero
        backdropAlpha = 0.95f,  // deja pasar casi el fondo
        borderAlpha = 0.22f,
        highlightAlpha = 0.10f
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalAlignment = Alignment.Top
        ) {

            AsyncImage(
                model = imageUrl,
                imageLoader = imageLoader,
                contentDescription = "Imagen del negocio",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(120.dp)
                    .padding(end = 16.dp)
            )
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = title,
                        color = Color.Black,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                    Text(
                        text = type,
                        color = Color.DarkGray,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.End,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = description, color = Color.Gray, fontSize = 14.sp, maxLines = 4)
            }
        }
    }
}

/**
 * Menú inferior con accesos rápidos a **Tarjeta**, **Cupones** y **Usuario**.
 */
@Composable
fun BottomMenu(
    onOpenFavorites: () -> Unit,
    onOpenProfile: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .background(Color.White.copy(alpha = 0.15f))
            .padding(horizontal = 24.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { onOpenFavorites() }) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = "Favoritos",
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
        IconButton(onClick = { onOpenProfile() }) {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Usuario",
                tint = Color.White,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}
