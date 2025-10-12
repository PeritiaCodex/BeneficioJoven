package mx.itesm.beneficiojoven.model.data.remote.dto

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

data class LoginReq(val email: String, val password: String)