package mx.itesm.beneficiojoven.model.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface FilterClickDao {

    @Query("SELECT * FROM filter_clicks ORDER BY clickCount DESC LIMIT 3")
    fun observeTopThree(): Flow<List<FilterClickEntity>>

    @Query("SELECT clickCount FROM filter_clicks WHERE type = :type")
    suspend fun getClickCount(type: String): Int?

    @Upsert
    suspend fun upsert(entity: FilterClickEntity)

    // Transacción para incrementar de forma segura
    @Query("UPDATE filter_clicks SET clickCount = clickCount + 1 WHERE type = :type")
    suspend fun incrementClickCount(type: String): Int

    suspend fun increment(type: String) {
        // Si la fila no existe, 'incrementClickCount' devuelve 0 cambios.
        // En ese caso, la creamos (upsert).
        if (incrementClickCount(type) == 0) {
            upsert(FilterClickEntity(type = type, clickCount = 1))
        }
    }
}
