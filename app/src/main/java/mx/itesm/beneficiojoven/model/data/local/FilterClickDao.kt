package mx.itesm.beneficiojoven.model.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

/**
 * DAO para operaciones sobre la tabla [FilterClickEntity].
 *
 * Permite observar los filtros más populares e incrementar su conteo.
 */
@Dao
interface FilterClickDao {

    /**
     * Observa las 3 categorías de filtros más usadas, ordenadas por conteo descendente.
     * @return Un [Flow] que emite la lista de las 3 [FilterClickEntity] principales.
     */
    @Query("SELECT * FROM filter_clicks ORDER BY clickCount DESC LIMIT 3")
    fun observeTopThree(): Flow<List<FilterClickEntity>>

    /**
     * Obtiene el conteo actual de clics para un tipo específico.
     * @param type El nombre de la categoría.
     * @return El conteo actual, o `null` si la categoría no existe.
     */
    @Query("SELECT clickCount FROM filter_clicks WHERE type = :type")
    suspend fun getClickCount(type: String): Int?

    /**
     * Inserta o actualiza una entidad de conteo de clics.
     * @param entity La entidad a guardar.
     */
    @Upsert
    suspend fun upsert(entity: FilterClickEntity)

    /**
     * (Transacción interna) Incrementa en 1 el contador para un tipo de filtro.
     * @param type El nombre de la categoría a incrementar.
     * @return El número de filas afectadas (1 si existía, 0 si no).
     */
    @Query("UPDATE filter_clicks SET clickCount = clickCount + 1 WHERE type = :type")
    suspend fun incrementClickCount(type: String): Int

    /**
     * Lógica de transacción para incrementar un contador de forma segura.
     * Si la categoría no existe, la crea con un conteo de 1.
     * Si ya existe, solo incrementa su contador en 1.
     *
     * @param type El nombre de la categoría que fue seleccionada.
     */
    suspend fun increment(type: String) {
        // Si la fila no existe, 'incrementClickCount' devuelve 0 cambios.
        // En ese caso, la creamos (upsert).
        if (incrementClickCount(type) == 0) {
            upsert(FilterClickEntity(type = type, clickCount = 1))
        }
    }
}