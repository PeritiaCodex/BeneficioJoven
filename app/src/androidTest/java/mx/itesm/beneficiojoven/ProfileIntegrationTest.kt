package mx.itesm.beneficiojoven

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.*
import mx.itesm.beneficiojoven.view.MainActivity
import org.junit.Rule
import org.junit.Test

class ProfileIntegrationTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    private fun realizarLogin() {
        composeRule.onNodeWithText("Correo").performTextInput("user@tec.mx")
        composeRule.onNodeWithText("Contraseña").performTextInput("123456")
        composeRule.onNodeWithText("Iniciar sesión").performClick()
        composeRule.waitUntil(5_000) {
            composeRule.onAllNodes(hasText("Cupones")).fetchSemanticsNodes().isNotEmpty()
        }
    }

    @Test
    fun perfil_apagarNotificaciones_y_confirmar() {
        realizarLogin()

        // Abrir Perfil
        composeRule.onNodeWithText("Perfil").performClick()

        // Espera a que esté el switch
        composeRule.waitUntil(5_000) {
            composeRule.onAllNodes(hasTestTag("push_switch")).fetchSemanticsNodes().isNotEmpty()
        }
        val switch = composeRule.onNodeWithTag("push_switch")

        // --- Asegurar estado base: ON ---
        val estabaOn = runCatching { switch.assertIsOn(); true }.getOrDefault(false)
        if (!estabaOn) {
            // Si estaba OFF, lo prendemos (no muestra diálogo)
            switch.performClick()
            switch.assertIsOn()
        }

        // --- Flujo bajo prueba: apagar y confirmar ---
        switch.performClick() // OFF → abre diálogo
        composeRule.onNodeWithText("Desactivar notificaciones").assertExists()
        composeRule.onNodeWithText("Sí").performClick()
        switch.assertIsOff()
    }
}
