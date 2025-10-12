package mx.itesm.beneficiojoven.model.data.remote

import mx.itesm.beneficiojoven.model.data.remote.dto.AuthResponse
import mx.itesm.beneficiojoven.model.data.remote.dto.CouponDto
import mx.itesm.beneficiojoven.model.data.remote.dto.LoginReq
import okhttp3.ResponseBody
import retrofit2.http.*
import retrofit2.Response

/**
 * API de backend de Beneficio Joven.
 *
 * Define los endpoints principales para autenticación y gestión de cupones.
 * Las rutas se resuelven contra la `baseUrl` configurada en [RetrofitClient].
 *
 * @see RetrofitClient
 */
interface BackendApi {

    /**
     * Inicia sesión con correo y contraseña.
     *
     * @param body Cuerpo JSON con credenciales de login.
     * @return [Response] conteniendo [AuthResponse] con `token` y `role`.
     */
    @POST("auth/login")
    suspend fun login(@Body body: LoginReq): Response<AuthResponse>

    /**
     * Registra un usuario.
     *
     * No retorna token; el flujo típico es **registrar** y luego llamar a **login**.
     *
     * @param body Mapa con `email`, `password`, `role` y `profileData`.
     * @return [Response] genérico con el cuerpo de texto en crudo.
     */
    @POST("auth/register")
    suspend fun register(
        @Body body: Map<String, @JvmSuppressWildcards Any>
    ): Response<ResponseBody>

    /**
     * Lista todos los cupones disponibles.
     * @return Colección de [CouponDto].
     */
    @GET("coupons")
    suspend fun listCoupons(): List<CouponDto>

    /**
     * Lista cupones filtrados por comercio.
     *
     * @param merchantId Identificador (o nombre) del comercio.
     * @return Colección de [CouponDto] del comercio.
     */
    @GET("coupons/merchant/{merchantId}")
    suspend fun listByMerchant(@Path("merchantId") merchantId: String): List<CouponDto>

    /**
     * Valida un cupón por su código.
     *
     * @param code Código a validar.
     * @return [CouponDto] correspondiente si es válido.
     */
    @GET("validar/{code}")
    suspend fun validate(@Path("code") code: String): CouponDto

    /**
     * Redime (canjea) un cupón.
     *
     * @param body Mapa con los parámetros requeridos para canje.
     * @return Mapa con el resultado del canje.
     */
    @POST("coupons/redeem")
    suspend fun redeem(@Body body: Map<String, Any>): Map<String, Any>
}
