// mx/itesm/beneficiojoven/model/data/repository/RemoteRepository.kt
package mx.itesm.beneficiojoven.model.data.repository

import mx.itesm.beneficiojoven.model.User
import mx.itesm.beneficiojoven.model.Role
import mx.itesm.beneficiojoven.model.data.remote.RetrofitClient
import mx.itesm.beneficiojoven.model.data.remote.Session
import mx.itesm.beneficiojoven.model.data.remote.dto.toDomain

class RemoteRepository : AppRepository {
    private val api = RetrofitClient.api

    override suspend fun login(email: String, password: String) = runCatching {
        val res = api.login(mapOf("email" to email, "password" to password))
        Session.token = res.token
        val role = when (res.role.lowercase()) {
            "merchant" -> Role.MERCHANT
            "admin", "super_admin" -> Role.ADMIN
            else -> Role.USER
        }
        User(id = "me", email = email, fullName = "", role = role)
    }

    override suspend fun register(
        name: String,
        email: String,
        password: String,
        curp: String,
        municipality: String,
        birthDate: String
    ) = runCatching {
        val profile = mapOf(
            "full_name" to name,
            "curp" to curp,
            "birth_date" to birthDate,
            "municipality" to municipality
        )
        api.register(
            mapOf(
                "email" to email,
                "password" to password,
                "role" to "user",
                "profileData" to profile
            )
        )
        // Auto-login
        val res = api.login(mapOf("email" to email, "password" to password))
        Session.token = res.token
        User(id = "me", email = email, fullName = name)
    }

    override suspend fun coupons() = runCatching {
        api.listCoupons().map { it.toDomain() }
    }

    // No hay GET /coupons/{id} en el backend; resolvemos localmente:
    override suspend fun couponById(id: String) = runCatching {
        val list = api.listCoupons()
        list.first { it.coupon_id.toString() == id }.toDomain()
    }
}
