// mx/itesm/beneficiojoven/model/data/repository/RemoteRepository.kt
package mx.itesm.beneficiojoven.model.data.repository

import mx.itesm.beneficiojoven.model.User
import mx.itesm.beneficiojoven.model.Role
import mx.itesm.beneficiojoven.model.UserProfile
import mx.itesm.beneficiojoven.model.data.remote.RetrofitClient
import mx.itesm.beneficiojoven.model.data.remote.Session
import mx.itesm.beneficiojoven.model.data.remote.dto.LoginReq
import mx.itesm.beneficiojoven.model.data.remote.dto.toDomain

/**
 * Implementación remota de [AppRepository] que consume el backend vía Retrofit.
 *
 * - Usa [RetrofitClient.api] para invocar endpoints.
 * - Guarda el JWT en [Session.token] tras autenticarse.
 * - Expone resultados envueltos en [Result] usando `runCatching { ... }`.
 */
class RemoteRepository : AppRepository {
    /** API remoto configurado con baseUrl, convertidores y auth interceptor. */
    private val api = RetrofitClient.api

    /**
     * Inicia sesión contra el backend.
     *
     * En éxito:
     * - Extrae el `token` y lo guarda en [Session.token].
     * - Mapea el rol textual a [Role].
     *
     * En error:
     * - Propaga el mensaje del servidor leyendo `errorBody()` cuando es posible.
     */
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

    /**
     * Registra un usuario y realiza **auto-login**.
     *
     * Flujo:
     * 1. Llama a `auth/register` con los datos del perfil (no devuelve token).
     * 2. Ejecuta `auth/login` con las mismas credenciales para obtener el token.
     * 3. Guarda el JWT en [Session.token] y retorna el [User] resultante.
     */
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

    /**
     * Recupera la lista de cupones desde el backend y los mapea a dominio.
     *
     * @return [Result] con la lista de cupones.
     */
    override suspend fun coupons() = runCatching {
        api.listCoupons().map { it.toDomain() }
    }

    /**
     * Obtiene un cupón por `id`.
     *
     * Nota: como no existe `GET /coupons/{id}` en el backend actual,
     * se hace **fetch** del listado completo y se filtra localmente.
     *
     * @param id Identificador del cupón.
     * @return [Result] con el cupón encontrado o error si no existe.
     */
    override suspend fun couponById(id: String) = runCatching {
        val list = api.listCoupons()
        list.first { it.coupon_id.toString() == id }.toDomain()
    }

    /**
     * Obtiene el perfil del usuario desde el endpoint protegido y lo mapea al modelo de dominio.
     */
    override suspend fun getProfile(): Result<UserProfile> = runCatching {
        val dto = api.getProfile()
        UserProfile(
            fullName = dto.full_name,
            email = dto.email,
            municipality = dto.municipality ?: "No especificado"
        )
    }
}
