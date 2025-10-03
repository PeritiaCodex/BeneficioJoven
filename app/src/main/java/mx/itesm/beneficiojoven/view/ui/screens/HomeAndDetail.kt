package mx.itesm.beneficiojoven.view.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import mx.itesm.beneficiojoven.model.Coupon
import mx.itesm.beneficiojoven.view.ui.theme.AppBackgroundCream
import mx.itesm.beneficiojoven.view.ui.theme.AppBarText
import mx.itesm.beneficiojoven.view.ui.theme.AppBarYellow
import mx.itesm.beneficiojoven.view.ui.theme.SoftDivider
import mx.itesm.beneficiojoven.vm.CouponListVM

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    vm: CouponListVM,
    onOpenCoupon: (String) -> Unit,
    onProfile: () -> Unit,
    onOpenFavorites: () -> Unit          // ← nuevo callback
) {
    val loading = vm.loading.collectAsState().value
    val error   = vm.error.collectAsState().value
    val coupons = vm.coupons.collectAsState().value

    Scaffold(
        containerColor = AppBackgroundCream,
        topBar = {
            TopAppBar(
                title = { Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) { Text("Cupones") } },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppBarYellow,
                    titleContentColor = AppBarText
                ),
                actions = {
                    TextButton(onClick = onProfile)       { Text("Perfil") }
                    TextButton(onClick = onOpenFavorites) { Text("Favoritos") } // ← usa la lambda
                }
            )
        }
    ) { pad ->
        when {
            loading -> Box(Modifier.fillMaxSize().padding(pad), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            error != null -> Box(Modifier.fillMaxSize().padding(pad), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Error: $error")
                    Spacer(Modifier.height(8.dp))
                    Button(onClick = vm::refresh) { Text("Reintentar") }
                }
            }
            else -> LazyColumn(
                Modifier.fillMaxSize().padding(pad),
                contentPadding = PaddingValues(8.dp)
            ) {
                items(coupons) { c ->
                    CouponCard(c) { onOpenCoupon(c.id) }
                    Divider(color = SoftDivider, thickness = 0.6.dp)
                }
            }
        }
    }
}

@Composable
private fun CouponCard(c: Coupon, onClick: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f)) {
            Text(c.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Text(c.description, style = MaterialTheme.typography.bodyMedium)
            Text(c.discountText, color = Color(0xFF1B5E20))
            if (c.validUntil != null) {
                Text("Vigente hasta ${c.validUntil}", style = MaterialTheme.typography.labelMedium)
            }
        }
        Spacer(Modifier.width(12.dp))
        AsyncImage(
            model = c.imageUrl ?: "https://picsum.photos/seed/${c.id}/200/120",
            contentDescription = c.title,
            modifier = Modifier
                .size(width = 120.dp, height = 72.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )
    }
}
