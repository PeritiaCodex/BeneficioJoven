package mx.itesm.beneficiojoven.model.data.remote.dto

/**
 * DTO de cupón tal como lo expone el backend.
 *
 * @property coupon_id Identificador entero del cupón.
 * @property code Código a mostrar/usar como “descuento”.
 * @property title Título del cupón.
 * @property description Descripción del cupón.
 * @property discount_type Tipo de descuento (puede ser nulo).
 * @property valid_from Inicio de vigencia (puede ser nulo).
 * @property valid_until Fin de vigencia (puede ser nulo).
 * @property usage_limit Límite de usos (puede ser nulo).
 * @property qr_code_url URL del código QR (puede ser nulo).
 * @property merchant_name Nombre del comercio.
 * @property merchant_logo URL del logo del comercio (puede ser nulo).
 * @property merchant_type Tipo/categoría del comercio (puede ser nulo).
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
 *
 * @property email Correo electrónico.
 * @property password Contraseña en texto plano.
 */
data class LoginReq(
    val email: String,
    val password: String
)

data class ProfileDto(
    val email: String,
    val full_name: String,
    val curp: String,
    val valid_from: String,
    val municipality: String
)