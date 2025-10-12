package mx.itesm.beneficiojoven.model.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * DAO para operaciones sobre la tabla de **cupones favoritos**.
 *
 * Incluye observación reactiva, inserción con reemplazo y borrado por id.
 */
@Dao
interface CouponDao {

    /**
     * Observa todas las entidades de favoritos ordenadas por fecha de guardado (desc).
     *
     * @return [Flow] con la lista de [FavCouponEntity].
     */
    @Query("SELECT * FROM fav_coupons ORDER BY savedAt DESC")
    fun observeFavorites(): Flow<List<FavCouponEntity>>

    /**
     * Observa si existe un favorito con el identificador dado.
     *
     * @param id Identificador del cupón.
     * @return [Flow] que emite `true` cuando existe; `false` si no existe.
     */
    @Query("SELECT EXISTS(SELECT 1 FROM fav_coupons WHERE id = :id)")
    fun isFavoriteFlow(id: String): Flow<Boolean>

    /**
     * Inserta una entidad de favorito. Si ya existe, la reemplaza.
     *
     * @param entity Entidad a insertar.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: FavCouponEntity)

    /**
     * Elimina un favorito por su identificador.
     *
     * @param id Identificador del cupón a eliminar.
     */
    @Query("DELETE FROM fav_coupons WHERE id = :id")
    suspend fun deleteById(id: String)
}
