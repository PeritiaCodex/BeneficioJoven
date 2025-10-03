package mx.itesm.beneficiojoven.view.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import mx.itesm.beneficiojoven.vm.AuthViewModel
import mx.itesm.beneficiojoven.view.ui.theme.AppBackgroundCream
import mx.itesm.beneficiojoven.view.ui.theme.AppBarText
import mx.itesm.beneficiojoven.view.ui.theme.AppBarYellow

@OptIn(ExperimentalMaterial3Api::class)
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
    var pass by remember { mutableStateOf("") }

    if (user != null) onLogged()

    Scaffold(
        containerColor = AppBackgroundCream,
        topBar = {
            TopAppBar(
                title = { Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) { Text("Beneficio Joven") } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = AppBarYellow, titleContentColor = AppBarText)
            )
        }
    ) { pad ->
        Column(Modifier.fillMaxSize().padding(pad).padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Correo") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = pass, onValueChange = { pass = it }, label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(8.dp))
            Button(onClick = { vm.login(email, pass) }, enabled = !loading, modifier = Modifier.fillMaxWidth()) { Text("Iniciar sesión") }
            TextButton(onClick = onForgot) { Text("¿Olvidaste tu contraseña?") }
            TextButton(onClick = onRegister) { Text("Crear cuenta") }
            TextButton(onClick = onTerms) { Text("Términos y condiciones") }
            if (loading) { Spacer(Modifier.height(8.dp)); CircularProgressIndicator() }
            if (error != null) { Spacer(Modifier.height(8.dp)); Text("Error: $error", color = MaterialTheme.colorScheme.error) }
        }
    }
}

@Composable fun RegisterScreen(onBack: () -> Unit) { SimpleInfo("Registro (prototipo)", onBack) }
@Composable fun ForgotScreen(onBack: () -> Unit) { SimpleInfo("Recuperar contraseña (prototipo)", onBack) }
@Composable fun TermsScreen(onBack: () -> Unit) { SimpleInfo("Términos y condiciones (prototipo)", onBack) }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SimpleInfo(text: String, onBack: () -> Unit) {
    Scaffold(
        containerColor = AppBackgroundCream,
        topBar = {
            TopAppBar(title = { Text(text) }, navigationIcon = { TextButton(onClick = onBack) { Text("Atrás") } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = AppBarYellow, titleContentColor = AppBarText))
        }
    ) { Box(Modifier.fillMaxSize().padding(it), contentAlignment = Alignment.Center) { Text(text) } }
}
