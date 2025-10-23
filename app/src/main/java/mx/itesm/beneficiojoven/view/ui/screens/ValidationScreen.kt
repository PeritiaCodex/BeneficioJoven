package mx.itesm.beneficiojoven.view.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import mx.itesm.beneficiojoven.view.ui.components.GradientScreenLayout
import mx.itesm.beneficiojoven.vm.ValidationViewModel

/**
 * Pantalla principal para roles de Merchant y Admin.
 *
 * Orquesta el escaneo de códigos QR y muestra el resultado de la validación.
 *
 * @param vm El ViewModel que maneja la lógica de validación de cupones.
 */
@Composable
fun ValidationScreen(
    vm: ValidationViewModel = viewModel()
) {
    val context = LocalContext.current
    val scanner = GmsBarcodeScanning.getClient(context)

    val validatedCoupon by vm.validatedCoupon.collectAsState()
    val isLoading by vm.isLoading.collectAsState()
    val error by vm.error.collectAsState()

    // El layout principal ahora controla el padding para que el fondo ocupe todo.
    GradientScreenLayout(contentPadding = PaddingValues(16.dp)) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (isLoading) {
                // THEME: Usar el color onPrimary del tema para el indicador.
                CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
            } else {
                Icon(
                    imageVector = Icons.Default.QrCodeScanner,
                    contentDescription = "Scanner Icon",
                    // THEME: Usar el color onPrimary del tema para el ícono principal.
                    tint = MaterialTheme.colorScheme.surfaceTint,
                    modifier = Modifier.size(120.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Panel de Validación",
                    // THEME: Usar la tipografía y color del tema.
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Presiona el botón para escanear el QR de un cupón y validarlo.",
                    // THEME: Usar la tipografía y color del tema con menor opacidad.
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(32.dp))

                // THEME: Aplicar colores del tema al botón.
                Button(
                    onClick = {
                        scanner.startScan()
                            .addOnSuccessListener { barcode ->
                                barcode.rawValue?.let {
                                    vm.validateCoupon(it)
                                }
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(context, "Error en el escáner: ${e.message}", Toast.LENGTH_LONG).show()
                            }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.onSecondary
                    )
                ) {
                    Text(
                        "Escanear QR",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White
                    )
                }
            }
        }
    }

    // --- Diálogos de resultado ---
    if (error != null) {
        ResultDialog(
            title = "Error de Validación",
            message = error ?: "El código no es válido.",
            onDismiss = { vm.clear() }
        )
    }

    if (validatedCoupon != null) {
        ResultDialog(
            title = "Cupón Válido",
            message = "Título: ${validatedCoupon!!.title}\nDescripción: ${validatedCoupon!!.description}",
            onDismiss = { vm.clear() }
        )
    }
}

/**
 * Un AlertDialog genérico para mostrar el resultado de una operación, estilizado con el tema.
 *
 * @param title Título del diálogo.
 * @param message Mensaje principal del diálogo.
 * @param onDismiss Acción a ejecutar cuando se cierra el diálogo.
 */
@Composable
private fun ResultDialog(title: String, message: String, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        // THEME: Aplicar colores del tema al diálogo.
        containerColor = MaterialTheme.colorScheme.surface,
        titleContentColor = MaterialTheme.colorScheme.onSurface,
        textContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        title = { Text(text = title, style = MaterialTheme.typography.headlineSmall) },
        text = { Text(text = message, style = MaterialTheme.typography.bodyMedium) },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Aceptar", fontWeight = FontWeight.Bold)
            }
        }
    )
}
