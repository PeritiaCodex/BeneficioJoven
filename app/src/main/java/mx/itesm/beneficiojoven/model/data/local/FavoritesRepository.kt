package mx.itesm.beneficiojoven.model.data.local

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import mx.itesm.beneficiojoven.model.Coupon
import mx.itesm.beneficiojoven.model.Merchant

/**
 * Repositorio local para gestionar **favoritos** usando Room.
 *
 * Expone flujos reactivamente (Flow) y provee métodos para observar,
 * agregar y eliminar cupones favoritos.
 *
 * @property dao DAO de acceso a la tabla de favoritos.
 * @see CouponDao
 */
class FavoritesRepository(private val dao: CouponDao) {

    /**
     * Observa la lista de favoritos ordenados por fecha de guardado (descendente),
     * mapeando cada entidad a un [Coupon] listo para la UI.
     *
     * @return [Flow] con la lista de [Coupon] “locales”.
     */
    fun observe(): Flow<List<Coupon>> =
        dao.observeFavorites().map { list ->
            list.map { e ->
                Coupon(
                    id = e.id,
                    title = e.title,
                    description = "Favorito",
                    imageUrl = null,
                    discountText = e.discountText,
                    merchant = Merchant("fav", "Favoritos", null, "Local"),
                    validUntil = null,
                    qrUrl = null
                )
            }
        }

    /**
     * Observa si un cupón específico está marcado como favorito.
     *
     * @param id Identificador del cupón.
     * @return [Flow] que emite `true` si es favorito; `false` en caso contrario.
     */
    fun isFavoriteFlow(id: String): Flow<Boolean> = dao.isFavoriteFlow(id)

    /**
     * Agrega (o reemplaza) un cupón como favorito.
     *
     * @param coupon Cupón de dominio a persistir como favorito.
     */
    suspend fun add(coupon: Coupon) =
        dao.insert(FavCouponEntity(coupon.id, coupon.title, coupon.discountText))

    /**
     * Elimina un cupón de favoritos por su identificador.
     *
     * @param id Identificador del cupón a eliminar.
     */
    suspend fun remove(id: String) = dao.deleteById(id)
}
