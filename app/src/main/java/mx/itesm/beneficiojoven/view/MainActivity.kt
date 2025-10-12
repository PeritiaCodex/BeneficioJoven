package mx.itesm.beneficiojoven.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import mx.itesm.beneficiojoven.view.ui.nav.AppNavHost
import mx.itesm.beneficiojoven.view.ui.theme.BeneficioJovenTheme

/*
 * Proyecto: Beneficio Joven
 * Archivo: <NOMBRE_DEL_ARCHIVO>.kt
 * Descripción: <Breve descripción del propósito del archivo>
 *
 * Autores:
 *  - Astrid Guadalupe Navarro Rojas | A01769650@tec.mx
 *  - Daniel Díaz Romero             | A01801486@tec.mx
 *  - David Alejandro Pérez Tabarés  | A01800971@tec.mx
 *  - Isaac Abud León                | A01801461@tec.mx
 *  - Juan Manuel Torres Rottonda    | A01800476@tec.mx
 *  - Luis Ángel Godínez González    | A01752310@tec.mx
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
 *
 * @see AppNavHost
 * @see BeneficioJovenTheme
 */
class MainActivity : ComponentActivity() {

    /**
     * Punto de entrada de la Activity.
     *
     * Configura el contenido con Jetpack Compose, aplicando el tema de la app y
     * preparando el controlador de navegación para el host principal.
     *
     * @param savedInstanceState Estado previamente guardado por el sistema (si existe).
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BeneficioJovenTheme {
                val nav = rememberNavController()
                AppNavHost(nav)
            }
        }
    }
}
