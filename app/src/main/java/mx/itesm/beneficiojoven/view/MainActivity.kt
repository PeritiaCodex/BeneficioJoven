package mx.itesm.beneficiojoven.view

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import mx.itesm.beneficiojoven.view.ui.nav.AppNavHost
import mx.itesm.beneficiojoven.view.ui.theme.BeneficioJovenTheme

/*
 * Proyecto: Beneficio Joven
 * Archivo: MainActivity.kt
 * Descripción: Inicializar la actividad principal de la aplicación.
 *
 * Autores:
 * - Astrid Guadalupe Navarro Rojas | A01769650@tec.mx
 * - Daniel Díaz Romero             | A01801486@tec.mx
 * - David Alejandro Pérez Tabarés  | A01800971@tec.mx
 * - Isaac Abud León                | A01801461@tec.mx
 * - Juan Manuel Torres Rottonda    | A01800476@tec.mx
 * - Luis Ángel Godínez González    | A01752310@tec.mx
 *
 * Fecha: 2025-10-11
 * Licencia: Propietaria (por definir)
 */


/**
 * Activity principal de la aplicación.
 *
 * Responsabilidades:
 * - Aplicar el tema [BeneficioJovenTheme].
 * - Crear y recordar un [androidx.navigation.NavHostController] con [rememberNavController].
 * - Inyectar el grafo de navegación en [AppNavHost].
 * - Solicitar permisos de notificación en Android 13+.
 *
 * @see AppNavHost
 * @see BeneficioJovenTheme
 */
class MainActivity : ComponentActivity() {

    /**
     * Launcher para gestionar el resultado de la solicitud de permisos.
     *
     * Se inicializa en [onCreate] para manejar la respuesta del usuario (concedida o denegada)
     * a la solicitud de permisos (específicamente [Manifest.permission.POST_NOTIFICATIONS]).
     * @see ActivityResultContracts.RequestPermission
     */
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    /**
     * Punto de entrada de la Activity.
     *
     * Configura el contenido con Jetpack Compose, aplicando el tema de la app y
     * preparando el controlador de navegación para el host principal.
     * También inicializa el [requestPermissionLauncher] y llama a [solicitarPermiso].
     *
     * @param savedInstanceState Estado previamente guardado por el sistema (si existe).
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission(),
        ) { isGranted: Boolean ->
            if (isGranted) {
                // El permiso fue concedido.
            } else {
                // Avisar que no habrá notificaciones
            }
        }

        solicitarPermiso()

        setContent {
            BeneficioJovenTheme {
                val nav = rememberNavController()
                AppNavHost(nav)
            }
        }
    }


    /**
     * Verifica y solicita el permiso de `POST_NOTIFICATIONS` si es necesario.
     *
     * Esta verificación solo se aplica en dispositivos con Android 13 (Tiramisu, API 33)
     * o superior. Si el permiso no está concedido, utiliza [requestPermissionLauncher]
     * para pedírselo al usuario.
     */
    private fun solicitarPermiso() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // El permiso ya está concedido.
            } else {
                // Solicitar el permiso.
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}