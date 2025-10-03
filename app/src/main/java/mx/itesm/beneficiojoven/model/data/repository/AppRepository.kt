package mx.itesm.beneficiojoven.model.data.repository

import mx.itesm.beneficiojoven.model.Coupon
import mx.itesm.beneficiojoven.model.User

interface AppRepository {
    suspend fun login(email: String, password: String): Result<User>
    suspend fun register(name: String, email: String, password: String): Result<User>
    suspend fun coupons(): Result<List<Coupon>>
    suspend fun couponById(id: String): Result<Coupon>
}