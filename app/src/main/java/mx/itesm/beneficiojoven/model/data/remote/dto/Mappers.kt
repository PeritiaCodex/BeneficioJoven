package mx.itesm.beneficiojoven.model.data.remote.dto

import mx.itesm.beneficiojoven.model.Coupon
import mx.itesm.beneficiojoven.model.Merchant

fun CouponDto.toDomain(): Coupon = Coupon(
    id = coupon_id.toString(),
    title = title,
    description = description,
    imageUrl = merchant_logo,
    discountText = code.takeIf { it.isNotBlank() } ?: (discount_type ?: "Descuento"),
    merchant = Merchant(
        id = merchant_name, name = merchant_name,
        logoUrl = merchant_logo, type = merchant_type ?: "Comercio"
    ),
    validUntil = valid_until,
    // Parche rápido si el backend dejó URLs absolutas de localhost en QR:
    qrUrl = qr_code_url?.replace("http://localhost:3000", "https://bj-api.site")
)