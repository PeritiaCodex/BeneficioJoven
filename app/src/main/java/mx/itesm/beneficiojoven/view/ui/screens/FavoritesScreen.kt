package mx.itesm.beneficiojoven.view.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import mx.itesm.beneficiojoven.model.Coupon
import mx.itesm.beneficiojoven.view.ui.theme.AppBackgroundCream
import mx.itesm.beneficiojoven.view.ui.theme.AppBarText
import mx.itesm.beneficiojoven.view.ui.theme.AppBarYellow
import mx.itesm.beneficiojoven.vm.FavoritesVM

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(onBack: () -> Unit, onOpenCoupon: (String) -> Unit) {
    val vm: FavoritesVM = viewModel()
    val list by vm.favorites.collectAsState()

    Scaffold(
        containerColor = AppBackgroundCream,
        topBar = {
            TopAppBar(
                title = { Text("Favoritos") },
                navigationIcon = { TextButton(onClick = onBack) { Text("Atrás") } },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppBarYellow, titleContentColor = AppBarText
                )
            )
        }
    ) { pad ->
        if (list.isEmpty()) Box(Modifier.fillMaxSize().padding(pad), contentAlignment = Alignment.Center) {
            Text("Aún no hay favoritos")
        } else LazyColumn(
            Modifier.fillMaxSize().padding(pad).padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(list) { c -> FavRow(c) { onOpenCoupon(c.id) } }
        }
    }
}

@Composable
private fun FavRow(c: Coupon, onClick: () -> Unit) {
    Surface(
        tonalElevation = 2.dp,
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)
    ) {
        Column(Modifier.padding(12.dp)) {
            Text(c.title, style = MaterialTheme.typography.titleMedium)
            Text(c.discountText, style = MaterialTheme.typography.bodyMedium)
        }
    }
}