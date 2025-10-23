package mx.itesm.beneficiojoven.view.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import mx.itesm.beneficiojoven.view.ui.components.AutoResizeText
import mx.itesm.beneficiojoven.view.ui.components.LiquidGlassCard
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
    val activeFilters by vm.activeFilters.collectAsState() // <- Obtener filtros activos
    val topFilters by vm.topFilters.collectAsState() // <- Obtener top 3

    // Carga inicial si no hay datos y no se está cargando.
    LaunchedEffect(Unit) { if (allCoupons.isEmpty() && !loading) vm.refresh() }

    // Agregación: cupones → negocios (primer cupón aporta logo/desc)
    val businesses: List<Business> = remember(allCoupons, activeFilters) {
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
            .filter { business ->
                activeFilters.isEmpty() || activeFilters.contains(business.type)
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
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.onSecondary)
                    }
                    error != null -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Error: $error", color = MaterialTheme.colorScheme.onError)
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
                        onToggle = { isFilterExpanded = !isFilterExpanded },
                        topFilters = topFilters, // <- Pasar top 3
                        activeFilters = activeFilters, // <- Pasar filtros activos
                        onFilterClick = vm::toggleFilter // <- Pasar acción
                    )
                    ExpandedFiltersPanel(
                        visible = isFilterExpanded,
                        activeFilters = activeFilters,
                        onFilterClick = vm::toggleFilter,
                        onClearFilters = vm::clearFilters
                    )
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
            color = MaterialTheme.colorScheme.outlineVariant,
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
fun FilterBar(
    isExpanded: Boolean,
    onToggle: () -> Unit,
    topFilters: List<String>,
    activeFilters: Set<String>,
    onFilterClick: (String) -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.65f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Muestra el top 3 de filtros
            topFilters.take(3).forEach { filter ->
                FilterChip(
                    label = filter,
                    onClick = { onFilterClick(filter) },
                    isSelected = activeFilters.contains(filter),
                    modifier = Modifier.weight(1f)
                )
            }
            // Rellena con chips vacíos si el top 3 no está completo
            repeat(3 - topFilters.size) {
                Spacer(modifier = Modifier.weight(1f))
            }
            IconToggleButton(checked = isExpanded, onCheckedChange = { onToggle() }) {
                Icon(
                    imageVector = if (isExpanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                    contentDescription = "Expandir filtros",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
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
fun ExpandedFiltersPanel(
    visible: Boolean,
    activeFilters: Set<String>,
    onFilterClick: (String) -> Unit,
    onClearFilters: () -> Unit
) {
    val allFilters = listOf(
        "Conveniencia", "Entretenimiento", "Supermercado", "Departamental",
        "Librería", "Restaurante", "Telecomunicaciones", "Deportes"
    ).sorted() // Ordenados alfabéticamente
    // 1. Define la altura de cada fila y el espaciado vertical
    val rowHeight = 40.dp // Altura de un chip + padding
    val verticalSpacing = 8.dp
    val numberOfColumns = 3 // <-- Fija el número de columnas a 3.

    // 2. Calcular el número de filas necesarias.
    val rowCount = ((allFilters.size - 1) / numberOfColumns) + 1

    // 3. Calcular la altura total de la grilla.
    // Altura de todas las filas + altura de todos los espaciados entre ellas.
    val gridHeight = (rowHeight * rowCount) + (verticalSpacing * (rowCount - 1))

    // --- FIN DE LA MODIFICACIÓN ---

    AnimatedVisibility(visible = visible) {
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onSecondary.copy(alpha = .5f)),
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium.copy(topStart = CornerSize(0), topEnd = CornerSize(0))
        ) {
            Column {
                LazyVerticalGrid(
                    // 4. Usa GridCells.Fixed con el número de columnas que definimos.
                    columns = GridCells.Fixed(numberOfColumns),
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(verticalSpacing),
                    // 5. Aplica la altura calculada, que ahora es correcta.
                    modifier = Modifier.height(gridHeight)
                ) {
                    items(allFilters) { filter ->
                        FilterChip(
                            label = filter,
                            onClick = { onFilterClick(filter) },
                            isSelected = activeFilters.contains(filter)
                        )
                    }
                }
                TextButton(
                    onClick = onClearFilters,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text("Limpiar filtros", color = MaterialTheme.colorScheme.onPrimary)
                }
                Spacer(Modifier.height(8.dp))
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
fun FilterChip(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false
) {
    val borderColor = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent
    val backgroundColor = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.3f) else MaterialTheme.colorScheme.surfaceTint

    Surface(
        color = backgroundColor,
        shape = MaterialTheme.shapes.small,
        modifier = modifier
            .clickable { onClick() }
            .border(width = 1.5.dp, color = borderColor, shape = MaterialTheme.shapes.small)
    ) {
        Box(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 8.dp), // El padding define el tamaño mínimo
            contentAlignment = Alignment.Center // Esto centra su contenido (el texto)
        ) {
            AutoResizeText(
                text = label,
                style = MaterialTheme.typography.labelMedium.copy(textAlign = TextAlign.Center),
                color = MaterialTheme.colorScheme.outlineVariant,
                modifier = Modifier
            )
        }
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

    LiquidGlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(22.dp),
        cornerRadius = 22.dp,
        blurRadius = 18.dp,
        tintAlpha = 0.04f,
        backdropAlpha = 0.95f,
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
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSecondary,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                    Text(
                        text = type,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.End,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 4
                )
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
            .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.25f))
            .padding(horizontal = 24.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { onOpenFavorites() }) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = "Favoritos",
                tint = MaterialTheme.colorScheme.surfaceTint,
                modifier = Modifier.size(32.dp)
            )
        }
        IconButton(onClick = { /* Cupones */ }) {
            Icon(
                imageVector = Icons.Default.LocalOffer,
                contentDescription = "Cupones",
                tint = MaterialTheme.colorScheme.surfaceTint,
                modifier = Modifier.size(32.dp)
            )
        }
        IconButton(onClick = { onOpenProfile() }) {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Usuario",
                tint = MaterialTheme.colorScheme.surfaceTint,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}
