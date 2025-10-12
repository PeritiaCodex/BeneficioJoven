package mx.itesm.beneficiojoven.model.data.remote.dto

import mx.itesm.beneficiojoven.model.Coupon
import mx.itesm.beneficiojoven.model.Merchant

// Mappers.kt
private fun normalizeLogoUrl(raw: String?): String? {
    if (raw.isNullOrBlank()) return null
    // Wikipedia “media viewer” → obtener el nombre real del archivo
    // Sirve para 'Archivo:' (ES) o 'File:' (EN). Nos quedamos con lo que está tras el último ':'
    return if (raw.contains("#/media/")) {
        val fileName = raw.substringAfter("#/media/").substringAfterLast(":")
        "https://commons.wikimedia.org/wiki/Special:FilePath/$fileName"
    } else raw
}

fun CouponDto.toDomain(): Coupon = Coupon(
    id = coupon_id.toString(),
    title = title,
    description = description,
    imageUrl = normalizeLogoUrl(merchant_logo),  // ← AQUÍ
    discountText = code.takeIf { it.isNotBlank() } ?: (discount_type ?: "Descuento"),
    merchant = Merchant(
        id = merchant_name,
        name = merchant_name,
        logoUrl = normalizeLogoUrl(merchant_logo), // ← y AQUÍ
        type = merchant_type ?: "Comercio"
    ),
    validUntil = valid_until,
    qrUrl = qr_code_url // tus QR ya son PNG directos: https://bj-api.site/qrcodes/...
)
