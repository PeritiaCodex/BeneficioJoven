package mx.itesm.beneficiojoven.view.ui.screens

import android.app.Application
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import mx.itesm.beneficiojoven.R
import mx.itesm.beneficiojoven.vm.AuthViewModel
import mx.itesm.beneficiojoven.vm.AuthViewModelFactory

@Composable
fun LoginScreen(
    vm: AuthViewModel = viewModel(factory = AuthViewModelFactory(LocalContext.current.applicationContext as Application)),
    onLogged: () -> Unit,
    onRegister: () -> Unit,
    onTerms: () -> Unit
) {
    val loading by vm.loading.collectAsState()
    val error by vm.error.collectAsState()
    val user by vm.user.collectAsState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var keepSession by remember { mutableStateOf(false) }

    LaunchedEffect(user) { if (user != null) onLogged() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF3B82F6), Color(0xFF6D28D9)) // azul → morado
                )
            ),
        contentAlignment = Alignment.Center
    ) {
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

            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.15f)),
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

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        placeholder = { Text("Correo electrónico") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        singleLine = true,
                        enabled = !loading,
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedPlaceholderColor = Color.LightGray,
                            unfocusedPlaceholderColor = Color.LightGray,
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            cursorColor = Color.White,
                            focusedIndicatorColor = Color.White,
                            unfocusedIndicatorColor = Color.White.copy(alpha = 0.6f)
                        )
                    )

                    Spacer(Modifier.height(8.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        placeholder = { Text("Contraseña") },
                        visualTransformation = PasswordVisualTransformation(),
                        singleLine = true,
                        enabled = !loading,
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedPlaceholderColor = Color.LightGray,
                            unfocusedPlaceholderColor = Color.LightGray,
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            cursorColor = Color.White,
                            focusedIndicatorColor = Color.White,
                            unfocusedIndicatorColor = Color.White.copy(alpha = 0.6f)
                        )
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
                                checkedColor = Color(0xFF3B82F6),
                                uncheckedColor = Color.LightGray,
                                checkmarkColor = Color.White
                            )
                        )
                        Text("Mantener la sesión iniciada", color = Color.White, fontSize = 14.sp)
                    }

                    Spacer(Modifier.height(12.dp))

                    Button(
                        onClick = { vm.login(email.trim(), password) },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !loading,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B82F6))
                    ) { Text(if (loading) "Entrando..." else "Login") }

                    Spacer(Modifier.height(8.dp))

                    Button(
                        onClick = onRegister,
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !loading,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6B7280))
                    ) { Text("Registrarse") }

                    Spacer(Modifier.height(8.dp))

                    // No hay pantalla de "Olvidaste contraseña" en el nav graph actual
                    // TextButton(onClick = onForgot, enabled = !loading) {
                    //     Text("¿Olvidaste tu contraseña?", color = Color.White)
                    // }

                    if (error != null) {
                        Spacer(Modifier.height(8.dp))
                        Text("Error: $error", color = MaterialTheme.colorScheme.error)
                    }
                }
            }

            Spacer(Modifier.height(12.dp))
            ClickableText(
                text = AnnotatedString("Términos y condiciones"),
                onClick = { onTerms() },
                style = TextStyle(
                    color = Color.White,
                    textDecoration = TextDecoration.Underline
                )
            )
        }

        if (loading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color.White)
            }
        }
    }
}
