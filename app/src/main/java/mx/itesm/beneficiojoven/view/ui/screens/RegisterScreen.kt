package mx.itesm.beneficiojoven.view.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mx.itesm.beneficiojoven.R
import mx.itesm.beneficiojoven.vm.AuthViewModel

/**
 * Pantalla de **registro de usuario**.
 *
 * Presenta un formulario con validaciones básicas (correo, contraseña, CURP, municipio y nombre),
 * aceptación de términos y, al confirmar, invoca [AuthViewModel.register]. Si el flujo concluye
 * con éxito (es decir, [AuthViewModel.user] deja de ser `null`), se ejecuta [onRegistered].
 *
 * El diseño utiliza un fondo con degradado, tarjeta translúcida y contenido desplazable.
 *
 * @param vm ViewModel responsable de la autenticación/registro.
 * @param onBack Acción para regresar a la pantalla previa.
 * @param onRegistered Acción a ejecutar tras registrarse (y típicamente hacer *auto-login*).
 * @see AuthViewModel.register
 */
@Composable
fun RegisterScreen(
    vm: AuthViewModel,
    onBack: () -> Unit = {},
    onRegistered: () -> Unit = {}
) {
    // Estado VM
    val loading by vm.loading.collectAsState()
    val error by vm.error.collectAsState()
    val user by vm.user.collectAsState()

    // Gradiente de fondo
    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(Color(0xFF4A90E2), Color(0xFF7B4397))
    )

    var curp by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var municipio by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var confirmarContrasena by remember { mutableStateOf("") }
    var aceptoTerminos by remember { mutableStateOf(false) }

    // Validaciones simples
    val emailOk = remember(correo) { correo.contains("@") && correo.contains(".") }
    val passOk = remember(contrasena) { contrasena.length >= 6 }
    val confirmOk = remember(contrasena, confirmarContrasena) { contrasena == confirmarContrasena }
    val curpOk = remember(curp) { curp.length == 18 }
    val muniOk = remember(municipio) { municipio.isNotBlank() }
    val nameOk = remember(nombre) { nombre.isNotBlank() }
    val formOk = emailOk && passOk && confirmOk && curpOk && muniOk && nameOk && aceptoTerminos && !loading

    // Navega cuando ya haya user
    LaunchedEffect(user) { if (user != null) onRegistered() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = backgroundBrush)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(5.dp))

            Image(
                painter = painterResource(id = R.drawable.logo_sf),
                contentDescription = "Logo Beneficio Joven",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .height(90.dp)
                    .width(90.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "BENEFICIO JOVEN",
                fontSize = 20.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0x8021212A)
                ),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Registro",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // --- Campos ---
                    OutlinedTextField(
                        value = curp,
                        onValueChange = { curp = it.uppercase() },
                        label = { Text("CURP") },
                        isError = curp.isNotBlank() && !curpOk,
                        supportingText = { if (curp.isNotBlank() && !curpOk) Text("Debe tener 18 caracteres") },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedLabelColor = Color.White,
                            unfocusedLabelColor = Color.Gray,
                            cursorColor = Color.White
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = nombre,
                        onValueChange = { nombre = it },
                        label = { Text("Nombre") },
                        isError = nombre.isNotBlank() && !nameOk,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedLabelColor = Color.White,
                            unfocusedLabelColor = Color.Gray,
                            cursorColor = Color.White
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = correo,
                        onValueChange = { correo = it },
                        label = { Text("Correo") },
                        isError = correo.isNotBlank() && !emailOk,
                        supportingText = { if (correo.isNotBlank() && !emailOk) Text("Correo no válido") },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedLabelColor = Color.White,
                            unfocusedLabelColor = Color.Gray,
                            cursorColor = Color.White
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = municipio,
                        onValueChange = { municipio = it },
                        label = { Text("Municipio") },
                        isError = municipio.isNotBlank() && !muniOk,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedLabelColor = Color.White,
                            unfocusedLabelColor = Color.Gray,
                            cursorColor = Color.White
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = contrasena,
                        onValueChange = { contrasena = it },
                        label = { Text("Contraseña (mín. 6)") },
                        visualTransformation = PasswordVisualTransformation(),
                        isError = contrasena.isNotBlank() && !passOk,
                        supportingText = { if (contrasena.isNotBlank() && !passOk) Text("Mínimo 6 caracteres") },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedLabelColor = Color.White,
                            unfocusedLabelColor = Color.Gray,
                            cursorColor = Color.White
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = confirmarContrasena,
                        onValueChange = { confirmarContrasena = it },
                        label = { Text("Confirmar contraseña") },
                        visualTransformation = PasswordVisualTransformation(),
                        isError = confirmarContrasena.isNotBlank() && !confirmOk,
                        supportingText = { if (confirmarContrasena.isNotBlank() && !confirmOk) Text("No coincide") },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedLabelColor = Color.White,
                            unfocusedLabelColor = Color.Gray,
                            cursorColor = Color.White
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Términos y Condiciones",
                        color = Color.White,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Checkbox(
                            checked = aceptoTerminos,
                            onCheckedChange = { aceptoTerminos = it },
                            colors = CheckboxDefaults.colors(
                                checkedColor = Color(0xFF4A90E2),
                                uncheckedColor = Color.LightGray,
                                checkmarkColor = Color.White
                            )
                        )
                        Text(
                            "Confirmo Términos y Condiciones",
                            color = Color.White,
                            fontSize = 12.sp
                        )
                    }

                    if (error != null) {
                        Spacer(Modifier.height(8.dp))
                        Text(error ?: "", color = MaterialTheme.colorScheme.error)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            vm.register(
                                name = nombre.trim(),
                                email = correo.trim(),
                                password = contrasena,
                                curp = curp.trim().uppercase(),
                                municipality = municipio.trim()
                            )
                        },
                        enabled = formOk,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4A90E2)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (loading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp,
                                color = Color.White
                            )
                            Spacer(Modifier.width(12.dp))
                        }
                        Text("Crear Cuenta", color = Color.White)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = onBack,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF7B4397)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Regresar", color = Color.White)
                    }
                }
            }
        }
    }
}

/**
 * *Preview* de la pantalla de registro.
 *
 * > Nota: está desactivado por depender de un [AuthViewModel].
 */
@Preview
@Composable
private fun RegisterScreenPreview() {
    // Preview sin VM (no interactivo)
    // RegisterScreen(vm = AuthViewModel())
}
