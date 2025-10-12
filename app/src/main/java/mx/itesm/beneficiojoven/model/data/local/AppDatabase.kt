package mx.itesm.beneficiojoven.model.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * Definición de la base de datos Room de la app (versión 1, sin exportar esquema).
 *
 * Contiene la tabla de [FavCouponEntity] y expone el [CouponDao].
 *
 * @see FavCouponEntity
 * @see CouponDao
 */
@Database(
    entities = [FavCouponEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    /** Provee el DAO para operar sobre la tabla de favoritos. */
    abstract fun couponDao(): CouponDao
}
