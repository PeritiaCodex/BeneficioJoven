package mx.itesm.beneficiojoven.view.ui.nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import mx.itesm.beneficiojoven.view.ui.screens.CouponDetailScreen
import mx.itesm.beneficiojoven.view.ui.screens.FavoritesScreen
import mx.itesm.beneficiojoven.view.ui.screens.ForgotScreen
import mx.itesm.beneficiojoven.view.ui.screens.HomeScreen
import mx.itesm.beneficiojoven.view.ui.screens.LoginScreen
import mx.itesm.beneficiojoven.view.ui.screens.ProfileScreen
import mx.itesm.beneficiojoven.view.ui.screens.RegisterScreen
import mx.itesm.beneficiojoven.view.ui.screens.TermsScreen
import mx.itesm.beneficiojoven.vm.AuthViewModel
import mx.itesm.beneficiojoven.vm.CouponListVM

@Composable
fun AppNavHost(nav: NavHostController) {
    val authVM = remember { AuthViewModel() }
    val listVM = remember { CouponListVM() }

    NavHost(navController = nav, startDestination = Screen.Login.route) {
        composable(Screen.Login.route) {
            LoginScreen(
                vm = authVM,
                onLogged = { nav.navigate(Screen.Home.route) { popUpTo(0) } },
                onRegister = { nav.navigate(Screen.Register.route) },
                onForgot = { nav.navigate(Screen.Forgot.route) },
                onTerms = { nav.navigate(Screen.Terms.route) }
            )
        }
        composable(Screen.Register.route) { RegisterScreen(onBack = { nav.popBackStack() }) }
        composable(Screen.Forgot.route)   { ForgotScreen(onBack = { nav.popBackStack() }) }
        composable(Screen.Terms.route)    { TermsScreen(onBack = { nav.popBackStack() }) }

        composable(Screen.Home.route) {
            HomeScreen(
                vm = listVM,
                onOpenCoupon = { id -> nav.navigate(Screen.CouponDetail.path(id)) },
                onProfile = { nav.navigate(Screen.Profile.route) },
                onOpenFavorites = { nav.navigate(Screen.Favorites.route) }
            )
        }

        composable(Screen.Profile.route) { ProfileScreen(onBack = { nav.popBackStack() }) }

        composable(Screen.Favorites.route) {
            FavoritesScreen(
                onBack = { nav.popBackStack() },
                onOpenCoupon = { id -> nav.navigate(Screen.CouponDetail.path(id)) }
            )
        }

        composable(Screen.CouponDetail.route) { backStack ->
            val id = backStack.arguments?.getString("id") ?: return@composable
            CouponDetailScreen(id = id, onBack = { nav.popBackStack() })
        }
    }
}
