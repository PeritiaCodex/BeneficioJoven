package mx.itesm.beneficiojoven.view.ui.nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import mx.itesm.beneficiojoven.view.ui.screens.BusinessesScreen
import mx.itesm.beneficiojoven.view.ui.screens.RegisterScreen
import mx.itesm.beneficiojoven.view.ui.screens.CouponScreen
import mx.itesm.beneficiojoven.view.ui.screens.FavoritesScreen
import mx.itesm.beneficiojoven.view.ui.screens.ForgotScreen
import mx.itesm.beneficiojoven.view.ui.screens.LoginScreen
import mx.itesm.beneficiojoven.view.ui.screens.ProfileScreen
import mx.itesm.beneficiojoven.view.ui.screens.TermsScreen
import mx.itesm.beneficiojoven.vm.AuthViewModel
import mx.itesm.beneficiojoven.vm.CouponListVM

/**
 * Host principal de navegación basado en **Navigation Compose**.
 *
 * Define el grafo de pantallas de la app y las transiciones entre ellas:
 *
 * - **Login** → al autenticarse, navega a [Screen.Businesses].
 * - **Register** → al registrarse, navega a [Screen.Businesses]; permite volver con *back*.
 * - **Forgot** / **Terms** / **Profile** → pantallas secundarias con *back*.
 * - **Businesses** → listado de negocios; desde aquí se navega a cupones por comercio.
 * - **CouponsByMerchant** → lista de cupones de un comercio específico.
 * - **Favorites** → lista de favoritos con acceso al detalle de cupón.
 * - **CouponDetail** → detalle de un cupón por `id`.
 *
 * @param nav Controlador de navegación de nivel superior.
 */
@Composable
fun AppNavHost(nav: NavHostController) {
    // ViewModels de alcance del host (alternativamente podrían inyectarse por DI/Hilt)
    val authVM = remember { AuthViewModel() }
    val listVM = remember { CouponListVM() }

    NavHost(navController = nav, startDestination = Screen.Login.route) {
        composable(Screen.Login.route) {
            LoginScreen(
                vm = authVM,
                onLogged = { nav.navigate(Screen.Businesses.route) { popUpTo(0) } },
                onRegister = { nav.navigate(Screen.Register.route) },
                onForgot = { nav.navigate(Screen.Forgot.route) },
                onTerms = { nav.navigate(Screen.Terms.route) }
            )
        }
        composable(Screen.Register.route) {
            RegisterScreen(
                vm = authVM,
                onRegistered = {
                    nav.navigate(Screen.Businesses.route) { popUpTo(0) }
                },
                onBack = { nav.popBackStack() }
            )
        }
        composable(Screen.Forgot.route)   { ForgotScreen(onBack = { nav.popBackStack() }) }
        composable(Screen.Terms.route)    { TermsScreen(onBack = { nav.popBackStack() }) }

        composable(Screen.Businesses.route) {
            BusinessesScreen(
                vm = listVM,
                onOpenMerchant = { merchant ->
                    nav.navigate(Screen.CouponsByMerchant.path(merchant))
                },
                onOpenFavorites = { nav.navigate(Screen.Favorites.route) }
            )
        }
        composable(Screen.CouponsByMerchant.route) { backStackEntry ->
            val merchant = backStackEntry.arguments?.getString("merchant")?.let { java.net.URLDecoder.decode(it, "UTF-8") } ?: return@composable
            CouponScreen(merchantName = merchant, vm = listVM, onBack = { nav.popBackStack() })
        }

        composable(Screen.Profile.route) { ProfileScreen(onBack = { nav.popBackStack() }) }

        composable(Screen.Favorites.route) {
            FavoritesScreen(
                onBack = { nav.popBackStack() },
                onOpenCoupon = { id -> nav.navigate(Screen.CouponDetail.path(id)) },
                onOpenFavorites = {  }
            )
        }
    }
}
