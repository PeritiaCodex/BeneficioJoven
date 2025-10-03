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

class FavoritesVM(app: Application) : AndroidViewModel(app) {
    private val repo = FavoritesRepository(InMemoryDbProvider.get(app).couponDao())

    val favorites: StateFlow<List<Coupon>> =
        repo.observe().stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    fun toggle(coupon: Coupon, nowFavorite: Boolean) = viewModelScope.launch {
        if (nowFavorite) repo.add(coupon) else repo.remove(coupon.id)
    }

    fun repo() = repo // para acceder a isFavoriteFlow desde UI del detalle
}