package mx.itesm.beneficiojoven.view.ui.nav

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import mx.itesm.beneficiojoven.model.Role
import mx.itesm.beneficiojoven.view.ui.screens.*
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
    val authVM: AuthViewModel = viewModel()
    val listVM: CouponListVM = viewModel()

    NavHost(navController = nav, startDestination = Screen.Login.route) {
        composable(Screen.Login.route) {
            LoginScreen(
                vm = authVM,
                onLogged = { user ->
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
        composable(Screen.Terms.route)    { TermsScreen(onBack = { nav.popBackStack() }) }

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

        composable(Screen.Profile.route) {
            ProfileScreen(
                onBack = { nav.popBackStack() },
                onLogout = {
                    nav.navigate(Screen.Login.route) {
                        popUpTo(0) // Limpia toda la pila de navegación
                    }
                }
            )
        }

        composable(Screen.Favorites.route) {
            FavoritesScreen(
                onBack = { nav.popBackStack() },
                onOpenCoupon = { id -> nav.navigate(Screen.CouponDetail.path(id)) },
                onOpenFavorites = {  },
                onOpenProfile = { nav.navigate(Screen.Profile.route) }
            )
        }

        composable(Screen.Validation.route) {
            ValidationScreen() // Llama a la pantalla que ya contiene su propia lógica.
        }
    }
}