package mx.itesm.beneficiojoven.model.data.repository

import mx.itesm.beneficiojoven.model.User
import mx.itesm.beneficiojoven.model.Role
import mx.itesm.beneficiojoven.model.data.remote.RetrofitClient
import mx.itesm.beneficiojoven.model.data.remote.Session
import mx.itesm.beneficiojoven.model.data.remote.dto.LoginReq
import mx.itesm.beneficiojoven.model.data.remote.dto.toDomain
import android.util.Log
import mx.itesm.beneficiojoven.model.Coupon
import mx.itesm.beneficiojoven.model.ResetPassword
import mx.itesm.beneficiojoven.model.data.remote.dto.MerchantProfileDto
import mx.itesm.beneficiojoven.model.data.remote.dto.SubscriptionToggleResponse

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
        Session.userId = body.id

        Log.d("DEBUG_PROFILE", "ID guardado en Sesión: ${Session.userId}")

        val role = when (body.role.lowercase()) {
            "merchant" -> Role.MERCHANT
            "admin", "super_admin" -> Role.ADMIN
            else -> Role.USER
        }
        User(id = body.id.toString(), email = email, fullName = "", role = role)
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
        Session.userId = body.id // Guardamos también el ID al registrar

        val role = when (body.role.lowercase()) {
            "merchant" -> Role.MERCHANT
            "admin", "super_admin" -> Role.ADMIN
            else -> Role.USER
        }
        User(id = body.id.toString(), email = email, fullName = name, role = role) // Usamos el ID real
    }

    /**
     * Cierra la sesión localmente borrando el token y el ID de usuario de [Session].
     */
    override suspend fun logout() {
        Session.token = null
        Session.userId = null
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
     * Obtiene los datos del perfil del usuario y los mapea al modelo de dominio [User].
     * @param userId El ID del usuario a consultar.
     * @return [Result] con el [User] mapeado o un error.
     */
    override suspend fun getProfile(userId: String) = runCatching {
        val dto = api.getProfile(userId)
        User( id = userId,
            email = dto.email,
            fullName = dto.full_name,
            municipality = dto.municipality
        )
    }

    /**
     * Valida un código de cupón contra el backend.
     * @param code El código (usualmente del QR) a validar.
     * @return [Result] con el [Coupon] mapeado si es válido, o un error.
     */
    override suspend fun validateCoupon(code: String) = runCatching {
        api.validate(code).toDomain()
    }

    /**
     * Solicita un correo de reseteo de contraseña.
     * @param email El correo del usuario.
     * @return [Result] con [Unit] en éxito, o un error.
     */
    override suspend fun requestPasswordReset(email: String) = runCatching {
        val body = mapOf("email" to email)
        val res = api.requestPasswordReset(body)
        if (!res.isSuccessful) {
            val err = res.errorBody()?.string().orEmpty()
            throw Exception("HTTP ${res.code()}: $err")
        }
        // Devuelve Unit en caso de éxito
    }

    /**
     * Restablece la contraseña usando el token y la nueva clave.
     * @param token El token recibido por correo.
     * @param newPassword La nueva contraseña.
     * @return [Result] con [Unit] en éxito, o un error.
     */
    override suspend fun resetPassword(token: String, newPassword: String) = runCatching {
        val body = ResetPassword(token = token, newPassword = newPassword)
        val res = api.resetPassword(body)
        if (!res.isSuccessful) {
            val err = res.errorBody()?.string().orEmpty()
            throw Exception("HTTP ${res.code()}: $err")
        }
        // Devuelve Unit en caso de éxito
    }

    /**
     * Actualiza el token de FCM (Firebase Cloud Messaging) del usuario en el backend.
     * @param fcmToken El nuevo token de dispositivo.
     * @return [Result] con [Unit] en éxito, o un error.
     */
    override suspend fun updateFcmToken(fcmToken: String) = runCatching {
        val body = mapOf("fcm_token" to fcmToken)
        val res = api.updateFcmToken(body)
        if (!res.isSuccessful) {
            val err = res.errorBody()?.string().orEmpty()
            throw Exception("HTTP ${res.code()}: $err")
        }
    }

    /**
     * Obtiene la lista completa de perfiles de comercios.
     * @return [Result] con la lista de [MerchantProfileDto] o un error.
     */
    override suspend fun listMerchants(): Result<List<MerchantProfileDto>> = runCatching {
        val response = api.listMerchants()
        if (!response.isSuccessful) {
            throw Exception("Error al obtener la lista de comercios: ${response.code()}")
        }
        response.body() ?: emptyList()
    }

    // --- Implementaciones de Suscripción ---

    /**
     * Obtiene la lista de IDs de comercios a los que el usuario está suscrito.
     * @return [Result] con la lista de IDs (Int) o un error.
     */
    override suspend fun getSubscribedMerchants(): Result<List<Int>> = runCatching {
        val response = api.getSubscribedMerchants()
        if (!response.isSuccessful) {
            throw Exception("Error al obtener suscripciones: ${response.code()}")
        }
        response.body() ?: emptyList()
    }

    /**
     * Alterna (suscribe/desuscribe) al usuario de un comercio.
     * @param merchantId El ID del comercio.
     * @return [Result] con la respuesta [SubscriptionToggleResponse] o un error.
     */
    override suspend fun toggleSubscription(merchantId: String): Result<SubscriptionToggleResponse> = runCatching {
        // La llamada a la API ya no necesita un body, solo el ID en la URL.
        val response = api.toggleSubscription(merchantId)
        if (!response.isSuccessful) {
            throw Exception("Error al cambiar la suscripción: ${response.code()}")
        }
        response.body() ?: throw IllegalStateException("La respuesta del servidor fue vacía")
    }
}