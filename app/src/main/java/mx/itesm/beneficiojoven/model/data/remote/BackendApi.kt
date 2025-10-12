package mx.itesm.beneficiojoven.model.data.remote

import mx.itesm.beneficiojoven.model.data.remote.dto.AuthResponse
import mx.itesm.beneficiojoven.model.data.remote.dto.CouponDto
import mx.itesm.beneficiojoven.model.data.remote.dto.LoginReq
import okhttp3.ResponseBody
import retrofit2.http.*
import retrofit2.Response


interface BackendApi {
    @POST("auth/login")
    suspend fun login(@Body body: LoginReq): Response<AuthResponse>
    @POST("auth/register")
    suspend fun register(
        @Body body: Map<String, @JvmSuppressWildcards Any>
    ): Response<ResponseBody>
    @GET("coupons")
    suspend fun listCoupons(): List<CouponDto>

    @GET("coupons/merchant/{merchantId}")
    suspend fun listByMerchant(@Path("merchantId") merchantId: String): List<CouponDto>

    @GET("validar/{code}")
    suspend fun validate(@Path("code") code: String): CouponDto

    @POST("coupons/redeem")
    suspend fun redeem(@Body body: Map<String, Any>): Map<String, Any>
}