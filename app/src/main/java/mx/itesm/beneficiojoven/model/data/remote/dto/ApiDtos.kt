package mx.itesm.beneficiojoven.model.data.remote.dto

/**
 * DTO de cupón tal como lo expone el backend.
 */
data class CouponDto(
    val coupon_id: Int,
    val code: String,
    val title: String,
    val description: String,
    val discount_type: String?,
    val valid_from: String?,
    val valid_until: String?,
    val usage_limit: Int?,
    val qr_code_url: String?,
    val merchant_name: String,
    val merchant_logo: String?,
    val merchant_type: String?
)

/**
 * Cuerpo de **login** enviado al endpoint de autenticación.
 */
data class LoginReq(
    val email: String,
    val password: String
)

/**
 * DTO del perfil de usuario "joven" devuelto por el backend.
 */
data class ProfileDto(
    val email: String,
    val full_name: String,
    val curp: String,
    val valid_from: String,
    val municipality: String
)

/**
 * DTO para la respuesta del endpoint de toggle-subscription.
 */
data class SubscriptionToggleResponse(
    val message: String,
    val subscribed: Boolean
)

/**
 * DTO para el perfil de un comercio, tal como lo expone el endpoint `/merchants`.
 */
data class MerchantProfileDto(
    val user_id: Int,       // Este es el ID numérico del merchant
    val merchant_name: String,
    val logo_url: String?,
    val merchant_type: String?
)