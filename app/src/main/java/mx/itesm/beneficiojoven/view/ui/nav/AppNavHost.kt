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
import mx.itesm.beneficiojoven.view.ui.screens.LoginScreen
import mx.itesm.beneficiojoven.view.ui.screens.ProfileScreen
import mx.itesm.beneficiojoven.view.ui.screens.TermsAndConditionsScreen
import mx.itesm.beneficiojoven.vm.AuthViewModel
import mx.itesm.beneficiojoven.vm.CouponListVM

@Composable
fun AppNavHost(nav: NavHostController) {
    val authVM = remember { AuthViewModel() }
    val listVM = remember { CouponListVM() }

    // Función para navegar a la pantalla de login y limpiar el backstack
    val goToLogin = {
        nav.navigate(Screen.Login.route) {
            popUpTo(0) { inclusive = true }
        }
    }

    NavHost(navController = nav, startDestination = Screen.Login.route) {
        composable(Screen.Login.route) {
            LoginScreen(
                vm = authVM,
                onLogged = {
                    nav.navigate(Screen.Businesses.route) { popUpTo(0) }
                },
                onRegister = { nav.navigate(Screen.Register.route) },
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
        composable(Screen.Terms.route) {
            TermsAndConditionsScreen(onBack = { nav.popBackStack() })
        }

        composable(Screen.Businesses.route) {
            BusinessesScreen(
                vm = listVM,
                onOpenMerchant = { merchant ->
                    nav.navigate(Screen.CouponsByMerchant.path(merchant))
                },
                onProfileClick = { nav.navigate(Screen.Profile.route) },
                onFavoritesClick = { nav.navigate(Screen.Favorites.route) }
            )
        }
        composable(Screen.CouponsByMerchant.route) { backStackEntry ->
            val merchant = backStackEntry.arguments?.getString("merchant")?.let { java.net.URLDecoder.decode(it, "UTF-8") } ?: return@composable
            CouponScreen(merchantName = merchant, vm = listVM, onBack = { nav.popBackStack() })
        }

        composable(Screen.Profile.route) {
            ProfileScreen(
                onBack = { nav.popBackStack() },
                onLogout = {
                    // Aquí podrías limpiar el token de sesión si lo estuvieras persistiendo
                    goToLogin()
                }
            )
        }

        composable(Screen.Favorites.route) {
            FavoritesScreen(
                onBack = { nav.popBackStack() },
                onOpenCoupon = { id -> nav.navigate(Screen.CouponDetail.path(id)) }
            )
        }

        // Puedes agregar la pantalla de detalle de cupón aquí si la necesitas
        composable(Screen.CouponDetail.route) { backStackEntry ->
            val couponId = backStackEntry.arguments?.getString("id")
            // Aquí iría tu Composable para el detalle del cupón, por ejemplo:
            // CouponDetailScreen(couponId = couponId, onBack = { nav.popBackStack() })
        }
    }
}
