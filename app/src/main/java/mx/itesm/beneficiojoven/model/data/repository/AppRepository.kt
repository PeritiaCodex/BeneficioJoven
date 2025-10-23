// mx/itesm/beneficiojoven/model/data/repository/AppRepository.kt
package mx.itesm.beneficiojoven.model.data.repository

import mx.itesm.beneficiojoven.model.Coupon
import mx.itesm.beneficiojoven.model.User

/**
 * Contrato de acceso a datos de la app.
 *
 * Define las operaciones de autenticación y consulta de cupones que
 * debe implementar cualquier repositorio (remoto, local, mixto).
 */
interface AppRepository {

    /**
     * Inicia sesión con credenciales.
     *
     * @param email Correo electrónico.
     * @param password Contraseña en texto plano.
     * @return [Result] que contiene el [User] autenticado en éxito; en fallo,
     *         incluye el detalle del error (por ejemplo, mensaje del backend).
     */
    suspend fun login(email: String, password: String): Result<User>

    /**
     * Registra un usuario y, de ser posible, devuelve la sesión iniciada.
     *
     * @param name Nombre completo.
     * @param email Correo electrónico.
     * @param password Contraseña en texto plano.
     * @param curp CURP del usuario.
     * @param municipality Municipio de residencia.
     * @param birthDate Fecha de nacimiento con formato `YYYY-MM-DD`.
     * @return [Result] con el [User] (típicamente ya autenticado) o un error.
     */
    suspend fun register(
        name: String,
        email: String,
        password: String,
        curp: String,
        municipality: String,
        birthDate: String = "2000-01-01"
    ): Result<User>

    /**
     * Obtiene la lista de cupones disponibles.
     *
     * @return [Result] con la colección de cupones o un error.
     */
    suspend fun coupons(): Result<List<mx.itesm.beneficiojoven.model.Coupon>>

    /**
     * Recupera un cupón por su identificador.
     *
     * @param id Identificador del cupón.
     * @return [Result] con el cupón o un error si no se encontró.
     */
    suspend fun couponById(id: String): Result<mx.itesm.beneficiojoven.model.Coupon>

    /** Obtiene los datos del perfil de un usuario.
     * @param userId ID del usuario.
     * @return [Result] con el [User] o un error.
     */
    suspend fun getProfile(userId: String): Result<User>

    /**
     * Valida un cupón a partir de su código.
     *
     * @param code El código del cupón a validar (obtenido del QR).
     * @return [Result] con el [Coupon] validado o un error si no es válido.
     */
    suspend fun validateCoupon(code: String): Result<Coupon>

    /**
     * Solicita el restablecimiento de contraseña para un correo electrónico.
     *
     * @param email El correo electrónico del usuario.
     * @return [Result] con Unit en caso de éxito, o un error si falla.
     */
    suspend fun requestPasswordReset(email: String): Result<Unit>

    /**
     * Restablece la contraseña de un usuario usando un token y la nueva contraseña.
     *
     * @param token El token de verificación recibido por el usuario.
     * @param newPassword La nueva contraseña a establecer.
     * @return [Result] con Unit en caso de éxito, o un error si falla.
     */
    suspend fun resetPassword(token: String, newPassword: String): Result<Unit>

    suspend fun updateFcmToken(fcmToken: String): Result<Unit>

    suspend fun toggleSubscription(merchantId: String): Result<Any>
}
