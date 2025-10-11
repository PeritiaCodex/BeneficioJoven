package mx.itesm.beneficiojoven.model.data.remote

import mx.itesm.beneficiojoven.model.data.remote.dto.AuthResponse
import mx.itesm.beneficiojoven.model.data.remote.dto.CouponDto
import retrofit2.http.*

// BackendApi.kt
interface BackendApi {
    @POST("auth/login")
    suspend fun login(@Body body: Map<String, String>): AuthResponse
    @POST("auth/register")
    suspend fun register(@Body body: Map<String, @JvmSuppressWildcards Any>): Map<String, Any>
    @GET("coupons")
    suspend fun listCoupons(): List<CouponDto>

    @GET("coupons/merchant/{merchantId}")
    suspend fun listByMerchant(@Path("merchantId") merchantId: String): List<CouponDto>

    @GET("validar/{code}")
    suspend fun validate(@Path("code") code: String): CouponDto

    @POST("coupons/redeem")
    suspend fun redeem(@Body body: Map<String, Any>): Map<String, Any>
}
