package mx.itesm.beneficiojoven.model.data.remote

import mx.itesm.beneficiojoven.model.ResetPassword
import mx.itesm.beneficiojoven.model.data.remote.dto.AuthResponse
import mx.itesm.beneficiojoven.model.data.remote.dto.CouponDto
import mx.itesm.beneficiojoven.model.data.remote.dto.LoginReq
import mx.itesm.beneficiojoven.model.data.remote.dto.MerchantProfileDto
import mx.itesm.beneficiojoven.model.data.remote.dto.ProfileDto
import mx.itesm.beneficiojoven.model.data.remote.dto.SubscriptionToggleResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

/**
 * API de backend de Beneficio Joven.
 */
interface BackendApi {

    /** Autentica a un usuario y devuelve un [AuthResponse] con el token JWT. */
    @POST("auth/login")
    suspend fun login(@Body body: LoginReq): Response<AuthResponse>

    /** Registra un nuevo usuario en el sistema. */
    @POST("auth/register")
    suspend fun register(
        @Body body: Map<String, @JvmSuppressWildcards Any>
    ): Response<ResponseBody>

    /** Obtiene la lista completa de todos los cupones disponibles. */
    @GET("coupons")
    suspend fun listCoupons(): List<CouponDto>

    /** Obtiene la lista de todos los perfiles de comercios. */
    @GET("merchants") // <-- NUEVO ENDPOINT
    suspend fun listMerchants(): Response<List<MerchantProfileDto>>

    /** Obtiene los cupones filtrados por un ID de comercio específico. */
    @GET("coupons/merchant/{merchantId}")
    suspend fun listByMerchant(@Path("merchantId") merchantId: String): List<CouponDto>

    /** (Rol: Merchant/Admin) Valida un código de cupón. */
    @GET("validar/{code}")
    suspend fun validate(@Path("code") code: String): CouponDto

    /** (Rol: Merchant/Admin) Canjea un cupón. */
    @POST("coupons/redeem")
    suspend fun redeem(@Body body: Map<String, Any>): Map<String, Any>

    /** Obtiene el perfil del usuario "joven" (rol USER) autenticado. */
    @GET("joven/{userId}")
    suspend fun getProfile(@Path("userId") userId: String): ProfileDto

    /** Inicia el flujo de reseteo de contraseña enviando un email al usuario. */
    @POST ("auth/request-password-reset")
    suspend fun requestPasswordReset(@Body body: Map<String, String>): Response<ResponseBody>

    /** Completa el reseteo de contraseña usando el token y la nueva contraseña. */
    @POST("auth/reset-password")
    suspend fun resetPassword(@Body body: ResetPassword): Response<ResponseBody>

    /** Actualiza el token de Firebase Cloud Messaging (FCM) del usuario. */
    @PUT("profile/fcm-token")
    suspend fun updateFcmToken(@Body body: Map<String, String>): Response<ResponseBody>

    // --- Endpoints de Suscripción ---

    /** Obtiene la lista de IDs de comercios a los que el usuario está suscrito. */
    @GET("subscribed-merchants")
    suspend fun getSubscribedMerchants(): Response<List<Int>>

    /** Activa o desactiva la suscripción (notificaciones) a un comercio. */
    @POST("merchants/{merchantId}/toggle-subscription")
    suspend fun toggleSubscription(
        @Path("merchantId") merchantId: String
    ): Response<SubscriptionToggleResponse>
}