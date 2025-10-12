package mx.itesm.beneficiojoven.model.data.remote.dto

import mx.itesm.beneficiojoven.model.Coupon
import mx.itesm.beneficiojoven.model.Merchant

// Mappers.kt
private fun normalizeLogoUrl(raw: String?): String? {
    if (raw.isNullOrBlank()) return null

    if (raw.contains("#/media/")) {
        val fileName = raw.substringAfter("#/media/").substringAfterLast(":")
        return "https://commons.wikimedia.org/wiki/Special:FilePath/$fileName"
    }

    // Caso 2: página wiki de “Archivo:” o “File:”
    val archivoIdx = raw.indexOf("/wiki/Archivo:")
    val fileIdx = raw.indexOf("/wiki/File:")
    if (archivoIdx != -1 || fileIdx != -1) {
        val name = raw.substringAfterLast(":") // toma  tras el último ':'
        return "https://commons.wikimedia.org/wiki/Special:FilePath/$name"
    }

    return raw
}


fun CouponDto.toDomain(): Coupon = Coupon(
    id = coupon_id.toString(),
    title = title,
    description = description,
    imageUrl = merchant_logo,              // <— antes: normalizeLogoUrl(merchant_logo)
    discountText = code.takeIf { it.isNotBlank() } ?: (discount_type ?: "Descuento"),
    merchant = Merchant(
        id = merchant_name,
        name = merchant_name,
        logoUrl = merchant_logo,           // <— antes: normalizeLogoUrl(merchant_logo)
        type = merchant_type ?: "Comercio"
    ),
    validUntil = valid_until,
    qrUrl = qr_code_url
)