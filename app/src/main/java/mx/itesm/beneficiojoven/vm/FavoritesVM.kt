package mx.itesm.beneficiojoven.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import mx.itesm.beneficiojoven.model.Coupon
import mx.itesm.beneficiojoven.model.data.local.DatabaseProvider
import mx.itesm.beneficiojoven.model.data.local.FavoritesRepository

/**
 * ViewModel responsable de exponer y actualizar la lista de cupones **favoritos**.
 *
 * Internamente usa un repositorio local para persistir el estado de favoritos
 * durante la sesión de la app, observando los cambios en la base de datos y
 * proveyendo métodos para modificar la lista.
 *
 * @param app La instancia de [Application] necesaria para inicializar la base de datos.
 * @constructor Crea un [FavoritesVM] con acceso a un [FavoritesRepository] basado en Room.
 * @see FavoritesRepository
 * @see DatabaseProvider
 */
class FavoritesVM(app: Application) : AndroidViewModel(app) {

    /** Repositorio local para gestionar favoritos. */
    private val repo = FavoritesRepository(DatabaseProvider.get(app).couponDao())

    /**
     * Flujo en vivo de cupones favoritos.
     *
     * Se inicializa con una lista vacía y se mantiene activo durante todo el ciclo de vida del ViewModel,
     * emitiendo la lista actualizada de cupones favoritos cada vez que cambia.
     */
    val favorites: StateFlow<List<Coupon>> =
        repo.observe().stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    /**
     * Marca o desmarca un cupón como favorito.
     *
     * Lanza una corutina para realizar la operación de añadir o remover el cupón
     * del repositorio de favoritos en segundo plano.
     *
     * @param coupon El [Coupon] a modificar.
     * @param nowFavorite `true` para agregar a favoritos, `false` para remover.
     */
    fun toggle(coupon: Coupon, nowFavorite: Boolean) = viewModelScope.launch {
        if (nowFavorite) repo.add(coupon) else repo.remove(coupon.id)
    }

    /**
     * Expone el repositorio para casos puntuales de UI (por ejemplo, observar `isFavoriteFlow`).
     *
     * Permite a los componentes de la UI acceder a flujos específicos del repositorio,
     * como el estado de favorito de un cupón individual, sin exponer el ViewModel completo.
     *
     * @return Instancia del [FavoritesRepository] subyacente.
     */
    fun repo() = repo // para acceder a isFavoriteFlow desde UI del detalle
}
