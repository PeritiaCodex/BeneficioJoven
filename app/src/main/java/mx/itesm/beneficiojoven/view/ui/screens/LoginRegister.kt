package mx.itesm.beneficiojoven.view.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mx.itesm.beneficiojoven.R
import mx.itesm.beneficiojoven.model.User
import mx.itesm.beneficiojoven.vm.AuthViewModel

/**
 * Pantalla de **inicio de sesión** con estilo degradado y tarjeta translúcida.
 *
 * Gestiona el estado de entrada de **correo** y **contraseña**, permite marcar
 * “mantener sesión iniciada”, y orquesta la navegación a **registro**, **recuperación**
 * y **términos**. Cuando [vm.user] cambia a no nulo, invoca [onLogged].
 *
 * @param vm ViewModel de autenticación encargado de realizar el login.
 * @param onLogged Callback para navegar a la pantalla principal tras autenticarse.
 * @param onRegister Navegación a la pantalla de registro.
 * @param onForgot Navegación a recuperación de contraseña.
 * @param onTerms Navegación a términos y condiciones.
 */
@Composable
fun LoginScreen(
    vm: AuthViewModel,
    onLogged: (User) -> Unit,
    onRegister: () -> Unit,
    onForgot: () -> Unit,
    onTerms: () -> Unit
) {
    val loading by vm.loading.collectAsState()
    val error by vm.error.collectAsState()
    val user by vm.user.collectAsState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var keepSession by remember { mutableStateOf(false) }

    // Evita navegar múltiples veces en recomposición
    LaunchedEffect(user) { user?.let { onLogged(it) } }

    GradientScreenLayout {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_sf),
                contentDescription = "Logo Beneficio Joven",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(100.dp)
                    .padding(bottom = 8.dp)
            )

            Text("BENEFICIO JOVEN", color = Color.White, fontSize = 22.sp)

            Spacer(Modifier.height(24.dp))

            Surface(
                shape = CardDefaults.shape,
                shadowElevation = 8.dp,
                color = MaterialTheme.colorScheme.scrim.copy(alpha = 0.2f),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(24.dp)
                ) {
                    Text("Login", color = Color.White, fontSize = 20.sp)
                    Spacer(Modifier.height(16.dp))

                    val textFieldColors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.tertiary,
                        unfocusedBorderColor = Color.Gray,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedLabelColor = MaterialTheme.colorScheme.onTertiary,
                        unfocusedLabelColor = Color.LightGray,
                        cursorColor = MaterialTheme.colorScheme.tertiary,
                    )

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Correo electrónico") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        singleLine = true,
                        enabled = !loading,
                        modifier = Modifier.fillMaxWidth(),
                        colors = textFieldColors
                    )

                    Spacer(Modifier.height(8.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Contraseña") },
                        visualTransformation = PasswordVisualTransformation(),
                        singleLine = true,
                        enabled = !loading,
                        modifier = Modifier.fillMaxWidth(),
                        colors = textFieldColors
                    )

                    Spacer(Modifier.height(8.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Checkbox(
                            checked = keepSession,
                            onCheckedChange = { keepSession = it },
                            enabled = !loading,
                            colors = CheckboxDefaults.colors(
                                checkedColor = MaterialTheme.colorScheme.onPrimary,
                                uncheckedColor = Color.LightGray,
                                checkmarkColor = MaterialTheme.colorScheme.onSecondary
                            )
                        )
                        Text("Mantener la sesión iniciada", color = Color.White, fontSize = 14.sp)
                    }

                    Spacer(Modifier.height(12.dp))

                    Button(
                        onClick = { vm.login(email.trim(), password) },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !loading,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.onPrimary,
                            contentColor = Color.White
                        )
                    ) { Text(if (loading) "Entrando..." else "Login") }

                    Spacer(Modifier.height(8.dp))

                    Button(
                        onClick = onRegister,
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !loading,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.onSecondary,
                            contentColor = Color.White
                        )
                    ) { Text("Registrarse") }

                    Spacer(Modifier.height(8.dp))

                    TextButton(onClick = onForgot, enabled = !loading) {
                        Text("¿Olvidaste tu contraseña?", color = Color.White)
                    }

                    if (error != null) {
                        Spacer(Modifier.height(8.dp))
                        Text("Error: $error", color = MaterialTheme.colorScheme.error)
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            TextButton(onClick = onTerms, enabled = !loading) {
                Text("Términos y condiciones", color = Color.White)
            }
        }

        if (loading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}

/**
 * Pantalla de **recuperación de contraseña** (prototipo).
 *
 * Encapsula un scaffold mínimo con título y botón de navegación atrás.
 *
 * @param onBack Acción de retorno a la pantalla previa.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotScreen(onBack: () -> Unit) {
    SimpleInfo("Recuperar contraseña (prototipo)", onBack)
}

/**
 * Utilidad de UI simple para mostrar una pantalla con **TopAppBar** y texto central.
 *
 * @param text Texto a mostrar como título y contenido.
 * @param onBack Acción de navegación hacia atrás.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SimpleInfo(
    text: String,
    onBack: () -> Unit
) {
    Scaffold(
        topBar =
            {
                TopAppBar(
                    title = { Text(text) },
                    navigationIcon = { TextButton(onClick = onBack) { Text("Atrás") } })
            }) { innerPadding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) { Text(text) }
    }
}
