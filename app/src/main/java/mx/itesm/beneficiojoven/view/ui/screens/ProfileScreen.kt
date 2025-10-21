package mx.itesm.beneficiojoven.view.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import mx.itesm.beneficiojoven.R
import mx.itesm.beneficiojoven.vm.ProfileViewModel

/**
 * Pantalla que muestra la información del perfil del usuario.
 *
 * Consume los datos del [ProfileViewModel] para mostrar el nombre, correo y
 * municipio del usuario. Incluye botones para editar el perfil y cerrar sesión.
 * También contiene una barra de navegación inferior.
 *
 * @param vm ViewModel que gestiona el estado y la carga de los datos del perfil.
 * @param onBack Callback para manejar la navegación hacia atrás.
 * @param onLogout Callback que se ejecuta cuando el usuario presiona "Cerrar sesión".
 * @param onOpenFavorites Callback para navegar a la pantalla de favoritos.
 * @param onOpenCoupons Callback para navegar a la pantalla de cupones.
 * @param onOpenProfile Callback para navegar a la pantalla de perfil (generalmente sin acción aquí).
 */
@Composable
fun ProfileScreen(
    vm: ProfileViewModel = viewModel(),
    onBack: () -> Unit,
    onLogout: () -> Unit,
    onOpenFavorites: () -> Unit = {},
    onOpenCoupons: () -> Unit = {},
    onOpenProfile: () -> Unit = {}
) {
    val profile by vm.profile.collectAsState()
    val loading by vm.loading.collectAsState()
    val error by vm.error.collectAsState()

    LaunchedEffect(Unit) { vm.loadProfile() }

    GradientScreenLayout(contentPadding = PaddingValues(0.dp)) {
        Column(modifier = Modifier.fillMaxSize()) {

            // 🔹 Barra superior
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 4.dp, end = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Regresar",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
                Text(
                    text = "Mi Perfil",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.width(48.dp))
            }

            // 🔸 Contenido principal
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                when {
                    loading -> CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
                    error != null -> Text("Error: $error", color = MaterialTheme.colorScheme.error)
                    else -> {
                        Image(
                            painter = painterResource(id = R.drawable.ic_launcher_foreground),
                            contentDescription = "Foto de perfil",
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
                                .padding(16.dp),
                            contentScale = ContentScale.Fit
                        )
                        Spacer(Modifier.height(16.dp))

                        Text(
                            text = profile?.fullName ?: "Nombre de usuario",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onPrimary,
                            textAlign = TextAlign.Center
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = profile?.email ?: "correo@ejemplo.com",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = profile?.municipality ?: "Municipio",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                        )

                        Spacer(Modifier.height(32.dp))

                        Button(
                            onClick = { /* TODO editar perfil */ },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.onPrimary,
                                contentColor = Color.White
                            )
                        ) {
                            Text(
                                "Editar perfil"
                            )
                        }

                        Spacer(Modifier.height(12.dp))

                        OutlinedButton(
                            onClick = onLogout,
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.onSecondary,
                                contentColor = Color.White
                            )
                        ) {
                            Text("Cerrar sesión")
                        }
                    }
                }
            }

            // 🔹 Menú inferior
            BottomMenu(
                onOpenFavorites = onOpenFavorites,
                onOpenCoupons = onOpenCoupons,
                onOpenProfile = onOpenProfile
            )
        }
    }
}

/**
 * Barra de navegación inferior con accesos a Favoritos, Cupones y Perfil.
 *
 * @param onOpenFavorites Acción para navegar a la pantalla de favoritos.
 * @param onOpenCoupons Acción para navegar a la pantalla de cupones.
 * @param onOpenProfile Acción para navegar a la pantalla de perfil.
 */
@Composable
private fun BottomMenu(
    onOpenFavorites: () -> Unit,
    onOpenCoupons: () -> Unit,
    onOpenProfile: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.15f))
            .padding(horizontal = 24.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onOpenFavorites) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = "Favoritos",
                tint = MaterialTheme.colorScheme.surfaceTint,
                modifier = Modifier.size(32.dp)
            )
        }
        IconButton(onClick = onOpenCoupons) {
            Icon(
                imageVector = Icons.Default.LocalOffer,
                contentDescription = "Cupones",
                tint = MaterialTheme.colorScheme.surfaceTint,
                modifier = Modifier.size(32.dp)
            )
        }
        IconButton(onClick = onOpenProfile) {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Usuario",
                tint = MaterialTheme.colorScheme.surfaceTint,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}
