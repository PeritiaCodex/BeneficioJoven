package mx.itesm.beneficiojoven.model.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CouponDao {
    @Query("SELECT * FROM fav_coupons ORDER BY savedAt DESC")
    fun observeFavorites(): Flow<List<FavCouponEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM fav_coupons WHERE id = :id)")
    fun isFavoriteFlow(id: String): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: FavCouponEntity)

    @Query("DELETE FROM fav_coupons WHERE id = :id")
    suspend fun deleteById(id: String)
}