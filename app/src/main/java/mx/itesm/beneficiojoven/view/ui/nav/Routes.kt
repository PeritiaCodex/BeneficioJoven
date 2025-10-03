package mx.itesm.beneficiojoven.view.ui.nav
sealed class Screen(val route: String) {
    data object Login : Screen("login")
    data object Register : Screen("register")
    data object Forgot : Screen("forgot")
    data object Terms : Screen("terms")
    data object Home : Screen("home")
    data object CouponDetail : Screen("coupon/{id}") { fun path(id: String) = "coupon/$id" }
    data object Profile : Screen("profile")
    data object Favorites : Screen("favorites")
}