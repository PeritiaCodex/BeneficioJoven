package mx.itesm.beneficiojoven.model.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [FavCouponEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun couponDao(): CouponDao
}