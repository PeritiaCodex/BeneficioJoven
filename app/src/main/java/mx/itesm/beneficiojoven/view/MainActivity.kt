package mx.itesm.beneficiojoven.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import mx.itesm.beneficiojoven.view.ui.nav.AppNavHost
import mx.itesm.beneficiojoven.view.ui.theme.BeneficioJovenTheme

class MainActivity : ComponentActivity() {
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