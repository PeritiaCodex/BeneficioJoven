package mx.itesm.beneficiojoven.model.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad Room para persistir **cupones favoritos** de forma ligera.
 *
 * Se almacena solo la información mínima necesaria para reconstruir
 * un [mx.itesm.beneficiojoven.model.Coupon] de tipo “local” en la UI.
 *
 * @property id Identificador del cupón (clave primaria).
 * @property title Título visible del cupón.
 * @property discountText Texto de descuento o código mostrado al usuario.
 * @property savedAt Marca de tiempo (epoch millis) cuando se guardó como favorito.
 */
@Entity(tableName = "fav_coupons")
data class FavCouponEntity(
    @PrimaryKey val id: String,
    val title: String,
    val discountText: String,
    val savedAt: Long = System.currentTimeMillis()
)
