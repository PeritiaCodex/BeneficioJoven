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
