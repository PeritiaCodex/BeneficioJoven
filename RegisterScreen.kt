package mx.itesm.beneficiojoven.view.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

@Composable
fun RegisterScreen(
    vm: AuthViewModel,
    onBack: () -> Unit = {},
    onRegistered: () -> Unit = {}
) {
    val loading by vm.loading.collectAsState()
    val error by vm.error.collectAsState()
    val user by vm.user.collectAsState()

    var curp by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var municipio by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var confirmarContrasena by remember { mutableStateOf("") }
    var aceptoTerminos by remember { mutableStateOf(false) }

    val emailOk = remember(correo) { correo.contains("@") && correo.contains(".") }
    val passOk = remember(contrasena) { contrasena.length >= 6 }
    val confirmOk = remember(contrasena, confirmarContrasena) { contrasena == confirmarContrasena }
    val curpOk = remember(curp) { curp.length == 18 }
    val muniOk = remember(municipio) { municipio.isNotBlank() }
    val nameOk = remember(nombre) { nombre.isNotBlank() }
    val formOk = emailOk && passOk && confirmOk && curpOk && muniOk && nameOk && aceptoTerminos && !loading

    LaunchedEffect(user) { if (user != null) onRegistered() }

    GradientScreenLayout {
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
                    containerColor = MaterialTheme.colorScheme.scrim.copy(alpha = 0.5f)
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

                    val textFieldColors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.LightGray,
                        focusedLabelColor = MaterialTheme.colorScheme.onTertiary,
                        unfocusedLabelColor = Color.LightGray,
                        cursorColor = MaterialTheme.colorScheme.onTertiary,
                        errorContainerColor = Color.Transparent,
                        focusedIndicatorColor = MaterialTheme.colorScheme.tertiary,
                        unfocusedIndicatorColor = Color.Gray.copy(alpha = 0.5f),
                        errorIndicatorColor = MaterialTheme.colorScheme.error
                    )

                    // --- Campos de texto ---
                    OutlinedTextField(
                        value = curp,
                        onValueChange = { curp = it.uppercase() },
                        label = { Text("CURP") },
                        isError = curp.isNotBlank() && !curpOk,
                        supportingText = { if (curp.isNotBlank() && !curpOk) Text("Debe tener 18 caracteres") },
                        colors = textFieldColors,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = nombre,
                        onValueChange = { nombre = it },
                        label = { Text("Nombre") },
                        isError = nombre.isNotBlank() && !nameOk,
                        colors = textFieldColors,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = correo,
                        onValueChange = { correo = it },
                        label = { Text("Correo") },
                        isError = correo.isNotBlank() && !emailOk,
                        supportingText = { if (correo.isNotBlank() && !emailOk) Text("Correo no válido") },
                        colors = textFieldColors,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = municipio,
                        onValueChange = { municipio = it },
                        label = { Text("Municipio") },
                        isError = municipio.isNotBlank() && !muniOk,
                        colors = textFieldColors,
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
                        colors = textFieldColors,
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
                        colors = textFieldColors,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // --- Texto clickeable de Términos y Condiciones ---
                    Text(
                        text = "Términos y Condiciones",
                        color = MaterialTheme.colorScheme.onTertiary,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .clickable { /* TODO: abrir enlace o modal */ }
                            .padding(vertical = 4.dp)
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Checkbox(
                            checked = aceptoTerminos,
                            onCheckedChange = { aceptoTerminos = it },
                            colors = CheckboxDefaults.colors(
                                checkedColor = MaterialTheme.colorScheme.onPrimary,
                                uncheckedColor = Color.LightGray,
                                checkmarkColor = MaterialTheme.colorScheme.onSecondary
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
                            containerColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (loading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                            Spacer(Modifier.width(12.dp))
                        }
                        Text("Crear Cuenta", color = Color.White)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = onBack,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.onSecondary
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Regresar", color = Color.LightGray)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun RegisterScreenPreview() {
    // Preview sin VM
    // RegisterScreen(vm = AuthViewModel())
}
