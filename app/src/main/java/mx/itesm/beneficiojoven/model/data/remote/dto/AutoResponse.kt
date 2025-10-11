package mx.itesm.beneficiojoven.model.data.remote.dto

data class AuthResponse(
    val token: String,
    val role: String
)