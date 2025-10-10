package mx.diz.asistenciaapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable // Importación añadida
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration // Importación añadida
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import androidx.compose.ui.text.input.PasswordVisualTransformation
import mx.diz.beneficiojovenui.GradientScreenLayout


@Composable
fun RegisterScreen(
    onBack: () -> Unit = {},
    onRegister: () -> Unit = {},
    // Añadimos un nuevo lambda para manejar el click en los términos
    onTermsClick: () -> Unit = {}
) {

    var curp by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var municipio by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var confirmarContrasena by remember { mutableStateOf("") }
    var aceptoTerminos by remember { mutableStateOf(false) }

    GradientScreenLayout {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(30.dp))

                // Logo
                AsyncImage(
                    model = "https://i.imgur.com/qqhj9gz_d.webp?maxwidth=760&fidelity=grand", // URL del logo
                    contentDescription = "Logo de la empresa",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .height(90.dp)
                        .width(90.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "BENEFICIO JOVEN",
                    fontSize = 20.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(15.dp))

                Text(
                    text = "Registro",
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(15.dp))

                // Contenedor
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
                        modifier = Modifier
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Campos de texto
                        OutlinedTextField(
                            value = curp,
                            onValueChange = { curp = it },
                            label = { Text("CURP") },
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
                            label = { Text("Contraseña") },
                            visualTransformation = PasswordVisualTransformation(),
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

                        // --- TEXTO CLICKABLE ---
                        Text(
                            text = "Términos y Condiciones",
                            color = Color.White,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                            textDecoration = TextDecoration.Underline, // Subrayado para indicar que es un enlace
                            modifier = Modifier.clickable { onTermsClick() } // Acción al hacer clic
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 8.dp)
                        ) {
                            Checkbox(
                                checked = aceptoTerminos,
                                onCheckedChange = { aceptoTerminos = it },
                                colors = CheckboxDefaults.colors(
                                    checkmarkColor = Color.White,
                                    uncheckedColor = Color.LightGray,
                                    checkedColor = Color(0xFF4A90E2) // Color del checkbox cuando está marcado
                                )
                            )
                            Text(
                                "Confirmo Términos y Condiciones",
                                color = Color.White,
                                fontSize = 12.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Button(
                            onClick = onRegister,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF4A90E2)
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
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
}
