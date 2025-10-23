package mx.itesm.beneficiojoven.view.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import mx.itesm.beneficiojoven.R
import mx.itesm.beneficiojoven.view.ui.components.GradientButton // Importa el GradientButton

// Define los pasos del flujo de recuperación
private enum class ForgotStep {
    EMAIL,
    CODE,
    PASSWORD
}

/**
 * Pantalla para el flujo de recuperación de contraseña.
 *
 * Guía al usuario a través de tres pasos:
 * 1. Ingresar su correo electrónico para recibir un código.
 * 2. Ingresar el código de verificación.
 * 3. Establecer una nueva contraseña.
 *
 * @param onBack Callback para manejar la navegación hacia atrás.
 */
@Composable
fun ForgotScreen(
    onBack: () -> Unit,
) {
    // Estado para controlar el paso actual del flujo
    var currentStep by remember { mutableStateOf(ForgotStep.EMAIL) }

    // Estados para los campos de texto
    var email by remember { mutableStateOf("") }
    var code by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    // Validaciones
    val passwordsMatch = newPassword.isNotEmpty() && newPassword == confirmPassword
    val isPasswordValid = newPassword.length >= 6

    // --- Definición de colores para consistencia ---
    val textFieldColors = TextFieldDefaults.colors(
        focusedContainerColor = Color.Transparent,
        unfocusedContainerColor = Color.Transparent,
        focusedTextColor = MaterialTheme.colorScheme.secondary,
        unfocusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
        cursorColor = MaterialTheme.colorScheme.secondary,
        focusedLabelColor = MaterialTheme.colorScheme.secondary,
        unfocusedLabelColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f),
        focusedIndicatorColor = MaterialTheme.colorScheme.secondary,
        unfocusedIndicatorColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f),
        errorIndicatorColor = MaterialTheme.colorScheme.error
    )

    GradientScreenLayout(contentPadding = PaddingValues(0.dp)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // --- Barra superior con flecha de regreso ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 4.dp)
            ) {
                IconButton(onClick = onBack, modifier = Modifier.align(Alignment.CenterStart)) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Regresar",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(5.dp))

            // --- Logo y Título ---
            Image(
                painter = painterResource(id = R.drawable.logo_sf),
                contentDescription = "Logo Beneficio Joven",
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(90.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            // Texto con estilo parcial en negritas
            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("BENEFICIO")
                    }
                    append(" JOVEN")
                },
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(12.dp))

            // --- Contenedor Principal (Surface) ---
            Surface(
                shape = CardDefaults.shape,
                shadowElevation = 8.dp,
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Recuperar contraseña",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(24.dp))

                    // --- Contenido animado que cambia según el paso ---
                    AnimatedContent(
                        targetState = currentStep,
                        label = "StepAnimation",
                        transitionSpec = {
                            slideInHorizontally { width -> width } togetherWith
                                    slideOutHorizontally { width -> -width }
                        }
                    ) { step ->
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            when (step) {
                                ForgotStep.EMAIL -> {
                                    OutlinedTextField(
                                        value = email,
                                        onValueChange = { email = it },
                                        label = { Text("Ingresa tu correo:") },
                                        modifier = Modifier.fillMaxWidth(),
                                        colors = textFieldColors,
                                        singleLine = true,
                                        keyboardOptions = KeyboardOptions(
                                            keyboardType = KeyboardType.Email,
                                            imeAction = ImeAction.Done
                                        )
                                    )
                                    Spacer(modifier = Modifier.height(24.dp))
                                    GradientButton(
                                        onClick = { currentStep = ForgotStep.CODE },
                                        modifier = Modifier.fillMaxWidth(),
                                        enabled = email.contains("@") && email.contains(".")
                                    ) {
                                        Text("Enviar")
                                    }
                                }

                                ForgotStep.CODE -> {
                                    OutlinedTextField(
                                        value = code,
                                        onValueChange = { code = it },
                                        label = { Text("Ingresa el código de verificación:") },
                                        modifier = Modifier.fillMaxWidth(),
                                        colors = textFieldColors,
                                        singleLine = true,
                                        keyboardOptions = KeyboardOptions(
                                            keyboardType = KeyboardType.Number,
                                            imeAction = ImeAction.Done
                                        )
                                    )
                                    Spacer(modifier = Modifier.height(24.dp))
                                    GradientButton(
                                        onClick = { currentStep = ForgotStep.PASSWORD },
                                        modifier = Modifier.fillMaxWidth(),
                                        enabled = code.length >= 4
                                    ) {
                                        Text("Siguiente")
                                    }
                                }

                                ForgotStep.PASSWORD -> {
                                    OutlinedTextField(
                                        value = newPassword,
                                        onValueChange = { newPassword = it },
                                        label = { Text("Contraseña nueva (mín. 6 caracteres):") },
                                        modifier = Modifier.fillMaxWidth(),
                                        colors = textFieldColors,
                                        visualTransformation = PasswordVisualTransformation(),
                                        singleLine = true,
                                        isError = newPassword.isNotEmpty() && !isPasswordValid
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    OutlinedTextField(
                                        value = confirmPassword,
                                        onValueChange = { confirmPassword = it },
                                        label = { Text("Confirmar contraseña:") },
                                        modifier = Modifier.fillMaxWidth(),
                                        colors = textFieldColors,
                                        visualTransformation = PasswordVisualTransformation(),
                                        singleLine = true,
                                        isError = confirmPassword.isNotEmpty() && !passwordsMatch
                                    )
                                    Spacer(modifier = Modifier.height(24.dp))
                                    GradientButton(
                                        onClick = {
                                            onBack()
                                        },
                                        modifier = Modifier.fillMaxWidth(),
                                        enabled = isPasswordValid && passwordsMatch
                                    ) {
                                        Text("Confirmar")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
