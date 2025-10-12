package mx.itesm.beneficiojoven.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val id: String,
    val email: String,
    val fullName: String,
    val role: Role = Role.USER
) : Parcelable

enum class Role { USER, MERCHANT, ADMIN }

@Parcelize
data class Merchant(
    val id: String,
    val name: String,
    val logoUrl: String?,
    val type: String
) : Parcelable

@Parcelize
data class Coupon(
    val id: String,
    val title: String,
    val description: String,
    val imageUrl: String?,
    val discountText: String,          // “15% OFF”, “$100 MXN”
    val merchant: Merchant,
    val validUntil: String?,           // simplificado
    val qrUrl: String?                 // para “ver QR” más adelante
) : Parcelable

data class AuthResponse(
    val token: String,
    val role: String
)

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

fun CouponDto.toDomain(): Coupon = Coupon(
    id = coupon_id.toString(),
    title = title,
    description = description,
    // Usa el logo del comercio como imagen por ahora
    imageUrl = merchant_logo,
    // Muestra el código o el tipo de descuento como texto de “descuento”
    discountText = code.takeIf { it.isNotBlank() } ?: (discount_type ?: "Descuento"),
    merchant = Merchant(
        id = merchant_name, // sin id real en la respuesta, usamos el nombre temporalmente
        name = merchant_name,
        logoUrl = merchant_logo,
        type = merchant_type ?: "Comercio"
    ),
    validUntil = valid_until,
    qrUrl = qr_code_url
)