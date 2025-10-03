package mx.itesm.beneficiojoven.model.data.repository

import mx.itesm.beneficiojoven.model.data.remote.RetrofitClient

class RemoteRepository : AppRepository {
    private val api = RetrofitClient.api

    override suspend fun login(email: String, password: String) = runCatching {
        api.login(mapOf("email" to email, "password" to password))
    }

    override suspend fun register(name: String, email: String, password: String) = runCatching {
        api.register(mapOf("name" to name, "email" to email, "password" to password))
    }

    override suspend fun coupons() = runCatching { api.listCoupons() }

    override suspend fun couponById(id: String) = runCatching { api.coupon(id) }
}