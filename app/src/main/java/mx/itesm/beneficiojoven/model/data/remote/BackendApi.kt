package mx.itesm.beneficiojoven.model.data.remote

import mx.itesm.beneficiojoven.model.ResetPassword
import mx.itesm.beneficiojoven.model.data.remote.dto.*
import okhttp3.ResponseBody
import retrofit2.http.*
import retrofit2.Response

/**
 * API de backend de Beneficio Joven.
 */
interface BackendApi {

    @POST("auth/login")
    suspend fun login(@Body body: LoginReq): Response<AuthResponse>

    @POST("auth/register")
    suspend fun register(
        @Body body: Map<String, @JvmSuppressWildcards Any>
    ): Response<ResponseBody>

    @GET("coupons")
    suspend fun listCoupons(): List<CouponDto>

    @GET("merchants") // <-- NUEVO ENDPOINT
    suspend fun listMerchants(): Response<List<MerchantProfileDto>>

    @GET("coupons/merchant/{merchantId}")
    suspend fun listByMerchant(@Path("merchantId") merchantId: String): List<CouponDto>

    @GET("validar/{code}")
    suspend fun validate(@Path("code") code: String): CouponDto

    @POST("coupons/redeem")
    suspend fun redeem(@Body body: Map<String, Any>): Map<String, Any>

    @GET("joven/{userId}")
    suspend fun getProfile(@Path("userId") userId: String): ProfileDto

    @POST ("auth/request-password-reset")
    suspend fun requestPasswordReset(@Body body: Map<String, String>): Response<ResponseBody>

    @POST("auth/reset-password")
    suspend fun resetPassword(@Body body: ResetPassword): Response<ResponseBody>

    @PUT("profile/fcm-token")
    suspend fun updateFcmToken(@Body body: Map<String, String>): Response<ResponseBody>

    // --- Endpoints de Suscripción ---

    @GET("subscribed-merchants")
    suspend fun getSubscribedMerchants(): Response<List<Int>>

    @POST("merchants/{merchantId}/toggle-subscription")
    suspend fun toggleSubscription(
        @Path("merchantId") merchantId: String
    ): Response<SubscriptionToggleResponse>
}
