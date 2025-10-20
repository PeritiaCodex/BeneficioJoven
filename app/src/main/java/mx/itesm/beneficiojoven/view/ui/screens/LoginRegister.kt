package mx.itesm.beneficiojoven.view.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mx.itesm.beneficiojoven.R
import mx.itesm.beneficiojoven.vm.AuthViewModel

@Composable
fun LoginScreen(
    vm: AuthViewModel,
    onLogged: () -> Unit,
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

    LaunchedEffect(user) { if (user != null) onLogged() }

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotScreen(onBack: () -> Unit) {
    SimpleInfo("Recuperar contraseña (prototipo)", onBack)
}

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
            }) {
        Box(
            Modifier
                .fillMaxSize()
                .padding(it),
            contentAlignment = Alignment.Center
        ) { Text(text) }
    }
}
