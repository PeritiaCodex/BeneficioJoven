package mx.diz.beneficiojoven.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import mx.diz.beneficiojovenui.GradientScreenLayout

@Composable
fun LoginScreen() {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var keepSession by remember { mutableStateOf(false) }

    GradientScreenLayout {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(24.dp).fillMaxSize() // Añadido fillMaxSize para centrar verticalmente
        ) {
            // LOGO desde URL
            AsyncImage(
                model = "https://i.imgur.com/qqhj9gz_d.webp?maxwidth=760&fidelity=grand",
                contentDescription = "Logo Beneficio Joven",
                modifier = Modifier
                    .size(100.dp)
                    .padding(bottom = 8.dp)
            )

            Text(
                text = "BENEFICIO JOVEN",
                color = Color.White,
                fontSize = 22.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Contenedor principal
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.15f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(24.dp)
                ) {
                    Text("Login", color = Color.White, fontSize = 20.sp)
                    Spacer(modifier = Modifier.height(16.dp))

                    // --- CAMPO DE CORREO CON COLORES MODIFICADOS ---
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        placeholder = { Text("Correo electrónico") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.colors(
                            // Color del texto que el usuario escribe
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            // Color del placeholder ("Correo electrónico")
                            focusedPlaceholderColor = Color.LightGray,
                            unfocusedPlaceholderColor = Color.LightGray,
                            // Color de fondo (transparente para que se vea el de la Card)
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            // Color del cursor
                            cursorColor = Color.White
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // --- CAMPO DE CONTRASEÑA CON COLORES MODIFICADOS ---
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        placeholder = { Text("Contraseña") },
                        visualTransformation = PasswordVisualTransformation(),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.colors(
                            // Color del texto que el usuario escribe
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            // Color del placeholder ("Contraseña")
                            focusedPlaceholderColor = Color.LightGray,
                            unfocusedPlaceholderColor = Color.LightGray,
                            // Color de fondo
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            // Color del cursor
                            cursorColor = Color.White
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth() // Para alinear el checkbox a la izquierda
                    ) {
                        Checkbox(
                            checked = keepSession,
                            onCheckedChange = { keepSession = it },
                            colors = CheckboxDefaults.colors(
                                checkedColor = Color(0xFF3B82F6),
                                checkmarkColor = Color.White,
                                uncheckedColor = Color.LightGray
                            )
                        )
                        Text("Mantener la sesión iniciada", color = Color.White, fontSize = 14.sp)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = { /* TODO */ },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B82F6))
                    ) {
                        Text("Login")
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = { /* TODO */ },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6B7280))
                    ) {
                        Text("Registrarse")
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    TextButton(onClick = { /* TODO */ }) {
                        Text("¿Olvidaste tu contraseña?", color = Color.White)
                    }
                }
            }
        }
    }
}
