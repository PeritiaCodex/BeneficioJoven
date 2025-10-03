package mx.itesm.beneficiojoven.view.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import kotlinx.coroutines.launch
import mx.itesm.beneficiojoven.model.di.ServiceLocator
import mx.itesm.beneficiojoven.model.Coupon
import mx.itesm.beneficiojoven.view.ui.theme.AppBackgroundCream
import mx.itesm.beneficiojoven.view.ui.theme.AppBarText
import mx.itesm.beneficiojoven.view.ui.theme.AppBarYellow
import mx.itesm.beneficiojoven.vm.FavoritesVM

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CouponDetailScreen(id: String, onBack: () -> Unit) {
    val scope = rememberCoroutineScope()
    val favVM: FavoritesVM = viewModel()

    // Cargar el cupón directo del repo (sin ViewModel extra)
    var coupon by remember { mutableStateOf<Coupon?>(null) }
    LaunchedEffect(id) {
        ServiceLocator.repo.couponById(id).onSuccess { coupon = it }
    }

    // ¿Es favorito?
    val isFav by remember(id) {
        favVM.repo().isFavoriteFlow(id)
    }.collectAsState(initial = false)

    Scaffold(
        containerColor = AppBackgroundCream,
        topBar = {
            TopAppBar(
                title = { Text("Detalle de cupón") },
                navigationIcon = { TextButton(onClick = onBack) { Text("Atrás") } },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppBarYellow,
                    titleContentColor = AppBarText
                )
            )
        }
    ) { pad ->
        if (coupon == null) {
            Box(Modifier.fillMaxSize().padding(pad), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }

        val c = coupon!!
        Column(
            Modifier.fillMaxSize().padding(pad).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AsyncImage(
                model = c.imageUrl ?: "https://picsum.photos/seed/${c.id}/800/400",
                contentDescription = c.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Text(c.title, style = MaterialTheme.typography.headlineSmall)
            Text(c.description)
            Text("Descuento: ${c.discountText}", color = MaterialTheme.colorScheme.primary)

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = { /* TODO: acción para canjear */ }) { Text("Obtener cupón") }
                OutlinedButton(onClick = {
                    scope.launch { favVM.toggle(c, !isFav) }
                }) {
                    Text(if (isFav) "Quitar de favoritos" else "Guardar favorito")
                }
            }
        }
    }
}
