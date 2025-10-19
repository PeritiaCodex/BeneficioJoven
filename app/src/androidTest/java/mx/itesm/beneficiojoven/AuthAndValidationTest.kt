package mx.itesm.beneficiojoven

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import mx.itesm.beneficiojoven.view.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Pruebas de instrumentación para los flujos de autenticación y validación.
 *
 * Estas pruebas se ejecutan en un dispositivo o emulador Android.
 */
@RunWith(AndroidJUnit4::class)
class AuthAndValidationTest {

    /**
     * Regla de Compose para interactuar con la UI. Lanza la MainActivity.
     */
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun loginAsMerchant_navigatesToValidationScreen() {
        // Credenciales del comerciante
        val merchantEmail = "comercio.nuevo@example.com"
        val merchantPassword = "NewMerchant123"

        // 1. Encontrar los campos y el botón usando sus testTags
        val emailField = composeTestRule.onNodeWithTag("email_input")
        val passwordField = composeTestRule.onNodeWithTag("password_input")
        val loginButton = composeTestRule.onNodeWithTag("login_button")

        // Esperar a que los elementos existan antes de interactuar
        emailField.assertExists()
        passwordField.assertExists()
        loginButton.assertExists()

        // 2. Escribir las credenciales y hacer clic
        emailField.performTextInput(merchantEmail)
        passwordField.performTextInput(merchantPassword)
        loginButton.performClick()

        // 3. Verificar el resultado: deberíamos estar en la pantalla de validación.
        // Esperamos a que aparezca el texto "Panel de Validación" que es único de esa pantalla.
        val validationScreenTitle = composeTestRule.onNodeWithText("Panel de Validación")
        
        // Espera un tiempo prudencial para la respuesta de la red y la navegación.
        validationScreenTitle.assertIsDisplayed()
    }

    @Test
    fun loginAsUser_navigatesToBusinessesScreen() {
        // Credenciales del usuario
        val userEmail = "ana.perez+001@ejemplo.com"
        val userPassword = "BjTest#123"

        // 1. Escribir las credenciales y hacer clic
        composeTestRule.onNodeWithTag("email_input").performTextInput(userEmail)
        composeTestRule.onNodeWithTag("password_input").performTextInput(userPassword)
        composeTestRule.onNodeWithTag("login_button").performClick()

        // 2. Esperar a que aparezca el título de la pantalla de negocios y luego verificar.
        // Esto soluciona problemas de timing con las llamadas de red.
        composeTestRule.waitUntil(timeoutMillis = 15_000) {
            composeTestRule
                .onAllNodesWithText("Catálogo de Negocios")
                .fetchSemanticsNodes().isNotEmpty()
        }
        
        // 3. Verificar que se navega a la pantalla de negocios
        composeTestRule.onNodeWithText("Catálogo de Negocios").assertIsDisplayed()
    }
}