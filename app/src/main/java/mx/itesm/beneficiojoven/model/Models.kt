package mx.itesm.beneficiojoven.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Modelo de usuario autenticado.
 *
 * @property id Identificador interno del usuario.
 * @property email Correo electrónico de la cuenta.
 * @property fullName Nombre completo.
 * @property role Rol funcional del usuario dentro de la app.
 */
@Parcelize
data class User(
    val id: String,
    val email: String,
    val fullName: String,
    val role: Role = Role.USER
) : Parcelable

/**
 * Modelo de dominio para los datos del perfil de usuario que se mostrarán en la UI.
 */
@Parcelize
data class UserProfile(
    val fullName: String,
    val email: String,
    val municipality: String,
    val profileImageUrl: String? = null // Reservado para el futuro
) : Parcelable

/**
 * Roles disponibles para control de capacidades de la app.
 *
 * - [USER] Usuario final.
 * - [MERCHANT] Comercio/negocio asociado a cupones.
 * - [ADMIN] Administrador del sistema.
 */
enum class Role { USER, MERCHANT, ADMIN }

/**
 * Modelo de **comercio**.
 *
 * @property id Identificador del comercio (puede ser el nombre si no hay id real).
 * @property name Nombre comercial.
 * @property logoUrl URL del logo (opcional; puede ser PNG/SVG).
 * @property type Tipo o categoría (por ejemplo, “Comida”, “Salud”).
 */
@Parcelize
data class Merchant(
    val id: String,
    val name: String,
    val logoUrl: String?,
    val type: String
) : Parcelable

/**
 * Modelo de **cupón** mostrado en la app.
 *
 * @property id Identificador del cupón (String para facilitar navegación/parcelado).
 * @property title Título del cupón.
 * @property description Descripción corta.
 * @property imageUrl Imagen asociada (opcional; hoy suele reutilizar el logo del comercio).
 * @property discountText Texto que se muestra como descuento (p. ej. código o “15% OFF”).
 * @property merchant Comercio propietario del cupón.
 * @property validUntil Fecha de vigencia (simplificada como `String`).
 * @property qrUrl URL del QR (si existe) para mostrar/compartir.
 */
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

/**
 * Respuesta de autenticación emitida por el backend tras login.
 *
 * @property token JWT emitido por el servidor.
 * @property role Rol asignado al usuario (cadena).
 */
data class AuthResponse(
    val token: String,
    val role: String
)

/**
 * DTO de cupón devuelto por el backend.
 *
 * Se mapea a [Coupon] mediante [toDomain].
 *
 * @property coupon_id Id entero del cupón.
 * @property code Código que puede mostrarse como “descuento”.
 * @property title Título.
 * @property description Descripción.
 * @property discount_type Tipo de descuento (opcional).
 * @property valid_from Inicio de vigencia (opcional).
 * @property valid_until Fin de vigencia (opcional).
 * @property usage_limit Límite de usos (opcional).
 * @property qr_code_url URL del QR generado por backend (opcional).
 * @property merchant_name Nombre del comercio.
 * @property merchant_logo Logo del comercio (opcional).
 * @property merchant_type Tipo/categoría del comercio (opcional).
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
 * Convierte un [CouponDto] (capa remota) al modelo de dominio [Coupon].
 *
 * - Usa el **logo del comercio** como `imageUrl`.
 * - Para [Coupon.discountText] prioriza `code`; si está vacío, cae a `discount_type`; si
 *   ambos no están presentes, usa “Descuento”.
 * - Construye [Merchant] con `merchant_name` como identificador provisional si no hay id real.
 *
 * @receiver [CouponDto] origen a convertir.
 * @return Instancia de [Coupon] lista para usarse en UI/navegación.
 */
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
