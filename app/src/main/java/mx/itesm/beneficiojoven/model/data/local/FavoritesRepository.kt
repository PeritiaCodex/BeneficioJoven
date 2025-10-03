package mx.itesm.beneficiojoven.model.data.local

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import mx.itesm.beneficiojoven.model.Coupon
import mx.itesm.beneficiojoven.model.Merchant

class FavoritesRepository(private val dao: CouponDao) {

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

    fun isFavoriteFlow(id: String): Flow<Boolean> = dao.isFavoriteFlow(id)

    suspend fun add(coupon: Coupon) =
        dao.insert(FavCouponEntity(coupon.id, coupon.title, coupon.discountText))

    suspend fun remove(id: String) = dao.deleteById(id)
}