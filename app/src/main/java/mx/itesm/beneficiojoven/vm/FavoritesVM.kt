package mx.itesm.beneficiojoven.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import mx.itesm.beneficiojoven.model.data.local.FavoritesRepository
import mx.itesm.beneficiojoven.model.data.local.InMemoryDbProvider
import mx.itesm.beneficiojoven.model.Coupon

/**
 * ViewModel responsable de exponer y actualizar la lista de cupones **favoritos**.
 *
 * Internamente usa un repositorio local (en memoria) para persistir el estado de favoritos
 * durante la sesión de la app.
 *
 * @constructor Crea un [FavoritesVM] con acceso a un [FavoritesRepository] basado en Room/In-memory.
 * @see FavoritesRepository
 * @see InMemoryDbProvider
 */
class FavoritesVM(app: Application) : AndroidViewModel(app) {

    /** Repositorio local para gestionar favoritos. */
    private val repo = FavoritesRepository(InMemoryDbProvider.get(app).couponDao())

    /**
     * Flujo en vivo de cupones favoritos.
     *
     * Se inicializa con una lista vacía y se mantiene activo durante todoo el ciclo del ViewModel.
     */
    val favorites: StateFlow<List<Coupon>> =
        repo.observe().stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    /**
     * Marca o desmarca un cupón como favorito.
     *
     * @param coupon Cupón a modificar.
     * @param nowFavorite `true` para agregar a favoritos, `false` para remover.
     */
    fun toggle(coupon: Coupon, nowFavorite: Boolean) = viewModelScope.launch {
        if (nowFavorite) repo.add(coupon) else repo.remove(coupon.id)
    }

    /**
     * Expone el repositorio para casos puntuales de UI (por ejemplo, observar `isFavoriteFlow`).
     *
     * @return Instancia del [FavoritesRepository] subyacente.
     */
    fun repo() = repo // para acceder a isFavoriteFlow desde UI del detalle
}
