package mx.itesm.beneficiojoven.view.ui.nav

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.firebase.messaging.FirebaseMessaging
import mx.itesm.beneficiojoven.model.Role
import mx.itesm.beneficiojoven.view.ui.screens.BusinessesScreen
import mx.itesm.beneficiojoven.view.ui.screens.CouponScreen
import mx.itesm.beneficiojoven.view.ui.screens.FavoritesScreen
import mx.itesm.beneficiojoven.view.ui.screens.ForgotScreen
import mx.itesm.beneficiojoven.view.ui.screens.LoginScreen
import mx.itesm.beneficiojoven.view.ui.screens.ProfileScreen
import mx.itesm.beneficiojoven.view.ui.screens.RegisterScreen
import mx.itesm.beneficiojoven.view.ui.screens.TermsScreen
import mx.itesm.beneficiojoven.view.ui.screens.ValidationScreen
import mx.itesm.beneficiojoven.vm.AuthViewModel
import mx.itesm.beneficiojoven.vm.CouponListVM

/**
 * Host principal de navegación basado en **Navigation Compose**.
 *
 * Define el grafo de pantallas de la app y las transiciones entre ellas:
 *
 * - **Login** → al autenticarse, navega a [Screen.Businesses] o [Screen.Validation] según el rol.
 * - **Register** → al registrarse, navega a [Screen.Businesses]; permite volver con *back*.
 * - **Forgot** / **Terms** / **Profile** → pantallas secundarias con *back*.
 * - **Businesses** → listado de negocios; desde aquí se navega a cupones por comercio.
 * - **CouponsByMerchant** → lista de cupones de un comercio específico.
 * - **Favorites** → lista de favoritos con acceso al detalle de cupón.
 * - **CouponDetail** → detalle de un cupón por `id`.
 * - **Validation** → pantalla para escanear y validar cupones (admins/merchants).
 *
 * @param nav Controlador de navegación de nivel superior.
 */
@Composable
fun AppNavHost(nav: NavHostController) {
    // ViewModels de alcance del host (alternativamente podrían inyectarse por DI/Hilt)

    /** ViewModel para gestionar la autenticación y estado del usuario. */
    val authVM: AuthViewModel = viewModel()
    /** ViewModel para gestionar la carga y estado de las listas (cupones, negocios). */
    val listVM: CouponListVM = viewModel()

    NavHost(navController = nav, startDestination = Screen.Login.route) {

        /**
         * Pantalla de **Login**.
         * Punto de entrada de la app. Al loguearse, obtiene el token FCM
         * y redirige según el rol del usuario ([Role.ADMIN] o [Role.MERCHANT]
         * van a [Screen.Validation], el resto a [Screen.Businesses]).
         */
        composable(Screen.Login.route) {
            LoginScreen(
                vm = authVM,
                onLogged = { user ->
                    FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val token = task.result
                            println("FCM TOKEN OBTENIDO: $token")
                            authVM.sendFcmToken(token)
                        } else {
                            println("Error al obtener el token FCM: ${task.exception}")
                        }
                    }
                    val destination = when (user.role) {
                        Role.ADMIN, Role.MERCHANT -> Screen.Validation.route
                        else -> Screen.Businesses.route
                    }
                    nav.navigate(destination) { popUpTo(0) }
                },
                onRegister = { nav.navigate(Screen.Register.route) },
                onForgot = { nav.navigate(Screen.Forgot.route) },
                onTerms = { nav.navigate(Screen.Terms.route) }
            )
        }

        /**
         * Pantalla de **Registro**.
         * Permite crear una nueva cuenta. Al registrarse exitosamente, navega
         * a la pantalla principal de negocios [Screen.Businesses].
         */
        composable(Screen.Register.route) {
            RegisterScreen(
                vm = authVM,
                onRegistered = {
                    // Por defecto, tras registrarse se va a la pantalla de usuario
                    nav.navigate(Screen.Businesses.route) { popUpTo(0) }
                },
                onBack = { nav.popBackStack() },
                onTerms = { nav.navigate(Screen.Terms.route)}
            )
        }

        /**
         * Pantalla de **Olvido de Contraseña**.
         * Permite al usuario solicitar un reseteo de contraseña.
         * Si el reseteo es exitoso, redirige de vuelta al [Screen.Login].
         */
        composable(Screen.Forgot.route)   {
            ForgotScreen(
                authViewModel = authVM,
                onBack = { nav.popBackStack() },
                onLoginRedirect = {
                    nav.navigate(Screen.Login.route) {
                        popUpTo(Screen.Login.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        /**
         * Pantalla de **Términos y Condiciones**.
         * Pantalla informativa estática con botón de regreso.
         */
        composable(Screen.Terms.route)    { TermsScreen(onBack = { nav.popBackStack() }) }

        /**
         * Pantalla principal para usuarios: **Lista de Negocios**.
         * Muestra los comercios disponibles. Permite navegar a:
         * - [Screen.CouponsByMerchant]: Cupones de un comercio específico.
         * - [Screen.Favorites]: Cupones favoritos del usuario.
         * - [Screen.Profile]: Perfil del usuario.
         */
        composable(Screen.Businesses.route) {
            BusinessesScreen(
                vm = listVM,
                onOpenMerchant = { merchant ->
                    nav.navigate(Screen.CouponsByMerchant.path(merchant))
                },
                onOpenFavorites = { nav.navigate(Screen.Favorites.route) },
                onOpenProfile = { nav.navigate(Screen.Profile.route) }
            )
        }

        /**
         * Pantalla de **Cupones por Comercio**.
         * Muestra la lista de cupones para un `merchant` específico,
         * obtenido de los argumentos de la ruta.
         */
        composable(Screen.CouponsByMerchant.route) { backStackEntry ->
            val merchant = backStackEntry.arguments?.getString("merchant")?.let { java.net.URLDecoder.decode(it, "UTF-8") } ?: return@composable
            CouponScreen(
                merchantName = merchant,
                vm = listVM,
                onBack = { nav.popBackStack() },
                onOpenFavorites = { nav.navigate(Screen.Favorites.route) }, // <-- He añadido los callbacks que faltaban
                onOpenProfile = { nav.navigate(Screen.Profile.route) }      // <-- en tu archivo original.
            )
        }

        /**
         * Pantalla de **Perfil de Usuario**.
         * Muestra información del usuario y permite cerrar sesión (`onLogout`).
         * Al cerrar sesión, limpia toda la pila de navegación y vuelve al [Screen.Login].
         */
        composable(Screen.Profile.route) {
            ProfileScreen(
                onBack = { nav.popBackStack() },
                onLogout = {
                    authVM.logout()
                    nav.navigate(Screen.Login.route) {
                        popUpTo(0) // Limpia toda la pila de navegación
                    }
                }
            )
        }

        /**
         * Pantalla de **Favoritos**.
         * Muestra los cupones guardados (favoritos) por el usuario.
         * Permite navegar al detalle de un cupón específico [Screen.CouponDetail].
         */
        composable(Screen.Favorites.route) {
            FavoritesScreen(
                onBack = { nav.popBackStack() },
                onOpenCoupon = { id -> nav.navigate(Screen.CouponDetail.path(id)) },
                onOpenFavorites = {  },
                onOpenProfile = { nav.navigate(Screen.Profile.route) }
            )
        }

        /**
         * Pantalla de **Validación** (para roles [Role.ADMIN] y [Role.MERCHANT]).
         * Contiene la lógica para escanear y validar cupones.
         */
        composable(Screen.Validation.route) {
            ValidationScreen() // Llama a la pantalla que ya contiene su propia lógica.
        }
    }
}