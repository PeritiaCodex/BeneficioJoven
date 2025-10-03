package mx.itesm.beneficiojoven.model.data.remote

import mx.itesm.beneficiojoven.model.Coupon
import mx.itesm.beneficiojoven.model.User
import retrofit2.http.*

interface BackendApi {
    @POST("auth/login")
    suspend fun login(@Body body: Map<String,String>): User

    @POST("auth/register")
    suspend fun register(@Body body: Map<String,String>): User

    @GET("coupons")
    suspend fun listCoupons(): List<Coupon>

    @GET("coupons/{id}")
    suspend fun coupon(@Path("id") id: String): Coupon
}