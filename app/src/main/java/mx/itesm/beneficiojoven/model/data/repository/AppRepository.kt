// mx/itesm/beneficiojoven/model/data/repository/AppRepository.kt
package mx.itesm.beneficiojoven.model.data.repository

import mx.itesm.beneficiojoven.model.User

interface AppRepository {
    suspend fun login(email: String, password: String): Result<User>

    // NUEVO: incluye curp, municipio (y birthDate opcional si luego lo pides)
    suspend fun register(
        name: String,
        email: String,
        password: String,
        curp: String,
        municipality: String,
        birthDate: String = "2000-01-01"
    ): Result<User>

    suspend fun coupons(): Result<List<mx.itesm.beneficiojoven.model.Coupon>>
    suspend fun couponById(id: String): Result<mx.itesm.beneficiojoven.model.Coupon>
}