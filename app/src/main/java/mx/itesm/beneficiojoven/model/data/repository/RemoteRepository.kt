package mx.itesm.beneficiojoven.model.data.repository

import mx.itesm.beneficiojoven.model.User
import mx.itesm.beneficiojoven.model.Role
import mx.itesm.beneficiojoven.model.data.remote.RetrofitClient
import mx.itesm.beneficiojoven.model.data.remote.Session
import mx.itesm.beneficiojoven.model.data.remote.dto.LoginReq
import mx.itesm.beneficiojoven.model.data.remote.dto.toDomain

class RemoteRepository : AppRepository {
    private val api = RetrofitClient.api

    override suspend fun login(email: String, password: String) = runCatching {
        val res = api.login(LoginReq(email.trim(), password))
        if (!res.isSuccessful) {
            val err = res.errorBody()?.string().orEmpty()
            throw Exception("HTTP ${res.code()}: $err")
        }
        val body = res.body() ?: error("Respuesta vacía")
        Session.token = body.token

        val role = when (body.role.lowercase()) {
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

        // 1) Registrar (aquí NO hay token)
        val reg = api.register(
            mapOf(
                "email" to email,
                "password" to password,
                "role" to "user",
                "profileData" to profile
            )
        )
        if (!reg.isSuccessful) {
            val err = reg.errorBody()?.string().orEmpty()
            throw Exception("HTTP ${reg.code()}: $err")
        }

        // 2) Auto-login (aquí SÍ obtenemos token)
        val loginRes = api.login(LoginReq(email.trim(), password))
        if (!loginRes.isSuccessful) {
            val err = loginRes.errorBody()?.string().orEmpty()
            throw Exception("HTTP ${loginRes.code()}: $err")
        }
        val body = loginRes.body() ?: error("Respuesta vacía")
        Session.token = body.token

        val role = when (body.role.lowercase()) {
            "merchant" -> Role.MERCHANT
            "admin", "super_admin" -> Role.ADMIN
            else -> Role.USER
        }
        User(id = "me", email = email, fullName = name, role = role)
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
