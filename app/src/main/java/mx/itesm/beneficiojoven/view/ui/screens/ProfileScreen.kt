package mx.itesm.beneficiojoven.view.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import mx.itesm.beneficiojoven.R
import mx.itesm.beneficiojoven.view.ui.components.GradientButton
import mx.itesm.beneficiojoven.view.ui.theme.LocalExtendedColors
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

    val diagonalGradient = LocalExtendedColors.current.diagonalGradientBrush

    LaunchedEffect(Unit) { vm.loadProfile() }

    GradientScreenLayout(contentPadding = PaddingValues(0.dp)) {
        Column(modifier = Modifier.fillMaxSize()) {
            // --- Barra Superior ---
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
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.size(32.dp)
                    )
                }
                Text(
                    text = "Mi Perfil",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        brush = diagonalGradient
                    ),
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.width(48.dp))
            }

            // --- Contenido Principal ---
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                when {
                    loading -> CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    error != null -> Text("Error: $error", color = MaterialTheme.colorScheme.error)
                    else -> {
                        // --- Imagen de Perfil (fuera de la tarjeta) ---
                        Image(
                            painter = painterResource(id = R.drawable.ic_launcher_foreground),
                            contentDescription = "Foto de perfil",
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.2f))
                                .padding(16.dp),
                            contentScale = ContentScale.Fit
                        )
                        Spacer(Modifier.height(24.dp))

                        // --- Tarjeta con la Información del Usuario ---
                        Surface(
                            shape = CardDefaults.shape,
                            shadowElevation = 8.dp,
                            color = MaterialTheme.colorScheme.surface,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 24.dp, horizontal = 16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = profile?.fullName ?: "Nombre de usuario",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(Modifier.height(8.dp))
                                Text(
                                    text = profile?.email ?: "correo@ejemplo.com",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f),
                                    textAlign = TextAlign.Center
                                )
                                Spacer(Modifier.height(4.dp))
                                Text(
                                    text = profile?.municipality ?: "Municipio",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }

                        Spacer(Modifier.height(32.dp))

                        // --- Botones de Acción ---
//                        GradientButton(
//                            onClick = { /* TODO: Navegar a pantalla de edición */ },
//                            modifier = Modifier.fillMaxWidth(),
//                            enabled = !loading
//                        ) {
//                            Text("Editar Perfil")
//                        }

                        Spacer(Modifier.height(12.dp))

                        GradientButton(
                            onClick = onLogout,
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Text("Cerrar sesión", color = MaterialTheme.colorScheme.onSecondary)
                        }
                    }
                }
            }

            // --- Menú Inferior Restaurado ---
            BottomMenu(
                onOpenFavorites = onOpenFavorites,
                onOpenCoupons = onOpenCoupons,
                onOpenProfile = onOpenProfile
            )
        }
    }
}

/**
 * Barra de navegación inferior con accesos a Tarjeta, Favoritos, Cupones y Perfil.
 * @param onOpenFavorites Callback para navegar a la pantalla de favoritos.
 * @param onOpenCoupons Callback para navegar a la pantalla de cupones.
 * @param onOpenProfile Callback para navegar a la pantalla de perfil.
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
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.size(32.dp)
            )
        }
        IconButton(onClick = onOpenCoupons) {
            Icon(
                imageVector = Icons.Default.LocalOffer,
                contentDescription = "Cupones",
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.size(32.dp)
            )
        }
        IconButton(onClick = onOpenProfile) {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Usuario",
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}
