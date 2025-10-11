package mx.itesm.beneficiojoven.view.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.ZeroCornerSize
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import androidx.compose.material.icons.filled.CreditCard


// Representa la información de un cupón
data class Bussiness(
    val title: String,
    val imageUrl: String,
    val description: String,
    val type: String
)

@Composable
fun BusinessesScreen(onCouponClick: () -> Unit = {}) {
    val coupons = remember {
        // ... tu lista de cupones se mantiene igual
        listOf(
            Bussiness("Six Flags - México", "https://pbs.twimg.com/profile_images/1828141276136443904/NsSVaImy_400x400.jpg", "Ven a Six Flags y aprovecha este Octoberfest...", "Entretenimiento"),
            Bussiness("Negocio A", "https://picsum.photos/200/200", "Descripción del cupón A", "Comida"),
            Bussiness("Negocio B", "https://picsum.photos/201/200", "Descripción del cupón B", "Salud"),
            Bussiness("Negocio C", "https://picsum.photos/202/200", "Descripción del cupón C", "Belleza"),
            Bussiness("Negocio X", "https://picsum.photos/203/200", "Descripción del cupón X", "Educación"),
            Bussiness("Negocio Y", "https://picsum.photos/204/200", "Descripción del cupón Y", "Moda y Accesorios"),
            Bussiness("Negocio Z", "https://picsum.photos/205/200", "Descripción del cupón Z", "Servicios")
        )
    }

    // Estado para controlar si el panel de filtros está expandido
    var isFilterExpanded by remember { mutableStateOf(false) }

    // DENTRO DE LA PLANTILLA...
    GradientScreenLayout {
        // Todo el contenido de la pantalla va dentro de este bloque
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Encabezado con botón de favoritos
            Header(title = "Catálogo de Negocios")

            Spacer(modifier = Modifier.height(16.dp))

            // Contenedor Box para manejar la superposición (zIndex)
            Box(modifier = Modifier.weight(1f)) {
                // Lista de cupones (siempre en el fondo)
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .zIndex(0f), // zIndex 0 para estar detrás
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                    contentPadding = PaddingValues(top = 80.dp, bottom = 80.dp) // Espacio para la barra de filtros
                ) {
                    items(coupons) { coupon ->
                        BussynessCard(
                            title = coupon.title,
                            imageUrl = coupon.imageUrl,
                            description = coupon.description,
                            type = coupon.type,
                            onClick = { onCouponClick() }
                        )
                    }
                }

                // Filtros (siempre encima)
                Column(modifier = Modifier.zIndex(1f)) { // zIndex 1 para estar delante
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


@Composable
fun Header() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Catálogo de Negocios:", color = Color.White, fontSize = 26.sp, fontWeight = FontWeight.Bold)
        IconButton(onClick = { /* TODO: acción de favoritos */ }) {
            Icon(imageVector = Icons.Default.Favorite, contentDescription = "Favoritos", tint = Color.White, modifier = Modifier.size(28.dp))
        }
    }
}

@Composable
fun FilterBar(isExpanded: Boolean, onToggle: () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.DarkGray.copy(alpha = 0.75f)),
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

            // Botón para expandir/contraer
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

@Composable
fun ExpandedFiltersPanel(visible: Boolean) {
    val allFilters = listOf("Entretenimiento", "Comida", "Salud", "Belleza", "Educación", "Moda", "Servicios", "Hogar", "Viajes")

    AnimatedVisibility(visible = visible) {
        Card(
            colors = CardDefaults.cardColors(containerColor = Color.DarkGray.copy(alpha = .75f)),
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium.copy(topStart = ZeroCornerSize, topEnd = ZeroCornerSize) // Bordes rectos arriba
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

@Composable
fun BussynessCard(title: String, imageUrl: String, onClick: () -> Unit, description: String, type: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f)),
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            AsyncImage(
                model = imageUrl,
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
                    Text(text = title, color = Color.Black, fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f, fill = false))
                    Text(text = type, color = Color.DarkGray, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, textAlign = TextAlign.End, modifier = Modifier.padding(start = 8.dp))
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = description, color = Color.Gray, fontSize = 14.sp, maxLines = 4)
            }
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
            Icon(imageVector = Icons.Default.CreditCard, contentDescription = "Tarjeta", tint = Color.White, modifier = Modifier.size(32.dp))
        }
        IconButton(onClick = { /* Cupones */ }) {
            Icon(imageVector = Icons.Default.LocalOffer, contentDescription = "Cupones", tint = Color.White, modifier = Modifier.size(32.dp))
        }
        IconButton(onClick = { /* Usuario */ }) {
            Icon(imageVector = Icons.Default.AccountCircle, contentDescription = "Usuario", tint = Color.White, modifier = Modifier.size(32.dp))
        }
    }
}