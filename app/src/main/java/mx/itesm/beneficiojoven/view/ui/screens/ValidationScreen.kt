package mx.itesm.beneficiojoven.view.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
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
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import mx.itesm.beneficiojoven.view.ui.components.GradientScreenLayout
import mx.itesm.beneficiojoven.vm.ValidationViewModel

/**
 * Pantalla principal para roles de Merchant y Admin.
 *
 * Orquesta el escaneo de códigos QR y muestra el resultado de la validación.
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

    GradientScreenLayout {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = Color.White)
            } else {
                Icon(
                    imageVector = Icons.Default.QrCodeScanner,
                    contentDescription = "Scanner Icon",
                    tint = Color.White,
                    modifier = Modifier.size(120.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Panel de Validación",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Presiona el botón para escanear el QR de un cupón y validarlo.",
                    color = Color.White.copy(alpha = 0.8f),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(32.dp))
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
                        .height(50.dp)
                ) {
                    Text("Escanear QR", fontSize = 18.sp)
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
 * Un AlertDialog genérico para mostrar el resultado de una operación.
 *
 * @param title Título del diálogo.
 * @param message Mensaje principal del diálogo.
 * @param onDismiss Acción a ejecutar cuando se cierra el diálogo.
 */
@Composable
private fun ResultDialog(title: String, message: String, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = title, fontWeight = FontWeight.Bold) },
        text = { Text(text = message) },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Aceptar")
            }
        }
    )
}
