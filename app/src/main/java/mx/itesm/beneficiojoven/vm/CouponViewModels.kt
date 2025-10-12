package mx.itesm.beneficiojoven.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import mx.itesm.beneficiojoven.model.di.ServiceLocator
import mx.itesm.beneficiojoven.model.Coupon

/**
 * ViewModel para la **lista de cupones**.
 *
 * Orquesta la carga inicial y las recargas manuales desde el repositorio
 * expuesto por [ServiceLocator]. Publica los estados de carga, error y la
 * colección actual de [Coupon].
 *
 * @see ServiceLocator
 */
class CouponListVM : ViewModel() {
    /** Repositorio compartido obtenido desde el Service Locator. */
    private val repo = ServiceLocator.repo

    /** Indicador de operación en curso. */
    private val _loading = MutableStateFlow(true)
    val loading: StateFlow<Boolean> = _loading

    /** Mensaje de error (o `null` si no hay). */
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    /** Lista observable de cupones para la UI. */
    private val _coupons = MutableStateFlow<List<Coupon>>(emptyList())
    val coupons: StateFlow<List<Coupon>> = _coupons

    /** Carga inicial de datos. */
    init { refresh() }

    /**
     * Vuelve a solicitar la lista de cupones al repositorio.
     *
     * En éxito, publica la lista en [coupons]; en error, publica el mensaje en [error].
     */
    fun refresh() = viewModelScope.launch {
        _loading.value = true; _error.value = null
        repo.coupons().onSuccess { _coupons.value = it }.onFailure { _error.value = it.message }
        _loading.value = false
    }
}

/**
 * ViewModel para **detalle de cupón**.
 *
 * Mantiene en [coupon] el cupón actualmente cargado, o `null` si no se ha solicitado.
 */
class CouponDetailVM : ViewModel() {
    /** Repositorio compartido obtenido desde el Service Locator. */
    private val repo = ServiceLocator.repo

    /** Cupón actual a mostrar en detalle (o `null`). */
    private val _coupon = MutableStateFlow<Coupon?>(null)
    val coupon: StateFlow<Coupon?> = _coupon

    /**
     * Carga un cupón por su identificador y lo publica en [coupon].
     *
     * @param id Identificador del cupón (como `String`).
     */
    suspend fun load(id: String) { repo.couponById(id).onSuccess { _coupon.value = it } }
}
