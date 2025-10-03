package mx.itesm.beneficiojoven.model.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fav_coupons")
data class FavCouponEntity(
    @PrimaryKey val id: String,
    val title: String,
    val discountText: String,
    val savedAt: Long = System.currentTimeMillis()
)