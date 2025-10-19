package mx.itesm.beneficiojoven.model.data.remote.dto

/**
 * Respuesta de autenticación devuelta por el endpoint de **login**.
 *
 * @property token JWT emitido por el servidor para futuras peticiones.
 * @property role Rol textual asignado al usuario autenticado.
 */
data class AuthResponse(
    val token: String,
    val role: String,
    val id: Int
)