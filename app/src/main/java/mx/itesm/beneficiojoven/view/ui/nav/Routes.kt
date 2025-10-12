package mx.itesm.beneficiojoven.view.ui.nav

/**
 * Representa las rutas de navegación de la aplicación usando una clase sellada.
 *
 * Cada objeto define su `route` para el grafo de Navigation Compose.
 * Algunas rutas incluyen parámetros de camino (p. ej. `coupon/{id}`) y
 * métodos auxiliares `path(...)` para construir rutas seguras desde código.
 */
sealed class Screen(val route: String) {

    /** Pantalla de inicio de sesión. */
    data object Login : Screen("login")

    /** Pantalla de registro de usuario. */
    data object Register : Screen("register")

    /** Pantalla de recuperación de contraseña. */
    data object Forgot : Screen("forgot")

    /** Pantalla de términos y condiciones. */
    data object Terms : Screen("terms")

    /**
     * Pantalla de detalle de cupón.
     *
     * @property route Ruta con placeholder `id`.
     * @see path
     */
    data object CouponDetail : Screen("coupon/{id}") {
        /**
         * Construye una ruta concreta para detalle de cupón.
         * @param id Identificador del cupón.
         * @return Ruta en formato `coupon/{id}` sustituyendo el parámetro.
         */
        fun path(id: String) = "coupon/$id"
    }

    /** Pantalla de perfil del usuario. */
    data object Profile : Screen("profile")

    /** Pantalla de cupones favoritos. */
    data object Favorites : Screen("favorites")

    /** Pantalla de listado de negocios (agregado por comercio). */
    data object Businesses : Screen("businesses")

    /** (Reservada) Ruta genérica de cupones. */
    data object Coupons : Screen("coupons")

    /**
     * Pantalla de cupones filtrados por **comercio**.
     *
     * La ruta incluye un parámetro `merchant` codificado en URL.
     */
    data object CouponsByMerchant : Screen("couponsFor/{merchant}") {
        /**
         * Construye la ruta con el nombre del comercio codificado.
         * @param merchant Nombre del comercio.
         * @return Ruta `couponsFor/{merchantCodificado}` lista para navegar.
         */
        fun path(merchant: String) =
            "couponsFor/${java.net.URLEncoder.encode(merchant, "UTF-8")}"
    }
}
