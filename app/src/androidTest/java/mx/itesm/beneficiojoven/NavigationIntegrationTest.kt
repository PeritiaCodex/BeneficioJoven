package mx.itesm.beneficiojoven

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.*
import mx.itesm.beneficiojoven.view.MainActivity
import org.junit.Rule
import org.junit.Test

class NavigationIntegrationTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun login_y_abrirPrimerCupon_muestraPantallaDetalle() {
        // 1) Login (usa tus propios labels de los TextFields)
        composeRule.onNodeWithText("Correo").performTextInput("test@tec.mx")
        composeRule.onNodeWithText("Contraseña").performTextInput("123456")
        composeRule.onNodeWithText("Iniciar sesión").performClick()

        // 2) Espera a que cargue Home (FakeRepository tiene delay)
        composeRule.waitUntil(timeoutMillis = 5_000) {
            composeRule.onAllNodes(hasText("Cupones")).fetchSemanticsNodes().isNotEmpty()
        }

        // 3) Toca el PRIMER cupón (en el fake repo viene “Latte 2x1”)
        composeRule.waitUntil(5_000) {
            composeRule.onAllNodes(hasText("Latte 2x1")).fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithText("Latte 2x1").performClick()

        // 4) Verifica que estamos en detalle y que aparece el título del cupón
        composeRule.waitUntil(5_000) {
            composeRule.onAllNodes(hasText("Detalle de cupón")).fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithText("Latte 2x1").assertExists()
    }
}