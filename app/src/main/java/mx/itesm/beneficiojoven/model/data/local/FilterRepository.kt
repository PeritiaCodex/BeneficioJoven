package mx.itesm.beneficiojoven.model.data.local

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Repositorio para gestionar las interacciones con los filtros de categorías.
 *
 * Encapsula la lógica de acceso a datos (DAO) para observar los filtros
 * más populares e incrementar sus contadores de uso.
 *
 * @property dao El Data Access Object ([FilterClickDao]) para la entidad [FilterClickEntity].
 */
class FilterRepository(private val dao: FilterClickDao) {

    /**
     * Observa el top 3 de filtros más usados.
     * @return Un Flow que emite una lista con los nombres (String) de las 3 categorías más populares.
     */
    fun observeTopThree(): Flow<List<String>> =
        dao.observeTopThree().map { entities -> entities.map { it.type } }

    /**
     * Incrementa el contador de clics para una categoría específica.
     * @param type El nombre de la categoría que fue seleccionada.
     */
    suspend fun incrementFilterClick(type: String) {
        dao.increment(type)
    }
}