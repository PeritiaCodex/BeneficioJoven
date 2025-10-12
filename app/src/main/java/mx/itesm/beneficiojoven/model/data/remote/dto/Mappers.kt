package mx.itesm.beneficiojoven.model.data.remote.dto

import mx.itesm.beneficiojoven.model.Coupon
import mx.itesm.beneficiojoven.model.Merchant

/**
 * (Helper interno) Normaliza URLs de Wikipedia/Commons cuando vienen como página
 * (`#/media/`, `/wiki/File:`, `/wiki/Archivo:`) y las convierte a la ruta
 * **Special:FilePath** que apunta al archivo real.
 *
 * Si la URL ya es directa, se regresa sin cambios.
 *
 * @param raw URL original (posiblemente nula o vacía).
 * @return URL normalizada o `null`.
 */
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

/**
 * Convierte un [CouponDto] de la capa remota al modelo de dominio [Coupon].
 *
 * - Usa el **logo del comercio** como `imageUrl` y `merchant.logoUrl`.
 * - Para [Coupon.discountText], prioriza `code`; si está vacío, cae a `discount_type`;
 *   si ambos faltan, usa `"Descuento"`.
 * - El `id` se serializa como `String` para facilitar navegación/parcelado.
 *
 * @receiver DTO remoto.
 * @return Modelo de dominio listo para UI.
 */
fun CouponDto.toDomain(): Coupon = Coupon(
    id = coupon_id.toString(),
    title = title,
    description = description,
    imageUrl = merchant_logo,              // <— se usa el valor directo del backend
    discountText = code.takeIf { it.isNotBlank() } ?: (discount_type ?: "Descuento"),
    merchant = Merchant(
        id = merchant_name,
        name = merchant_name,
        logoUrl = merchant_logo,           // <— se usa el valor directo del backend
        type = merchant_type ?: "Comercio"
    ),
    validUntil = valid_until,
    qrUrl = qr_code_url
)
