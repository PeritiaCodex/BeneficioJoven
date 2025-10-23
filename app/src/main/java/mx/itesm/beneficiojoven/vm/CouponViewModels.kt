package mx.itesm.beneficiojoven.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import mx.itesm.beneficiojoven.model.Coupon
import mx.itesm.beneficiojoven.model.data.local.DatabaseProvider
import mx.itesm.beneficiojoven.model.data.local.FilterRepository
import mx.itesm.beneficiojoven.model.di.ServiceLocator

/**
 * ViewModel para la **lista de cupones**.
 *
 * Orquesta la carga inicial, recargas y la **lógica de filtrado** de cupones.
 * - Carga cupones desde [ServiceLocator].
 * - Gestiona estados de carga, error y la lista de cupones.
 * - Maneja un estado de filtros activos.
 * - Interactúa con [FilterRepository] para guardar y obtener las categorías más usadas.
 *
 * @param application Requerido por AndroidViewModel para obtener el contexto.
 */
class CouponListVM(application: Application) : AndroidViewModel(application) {
    /** Repositorio remoto obtenido desde el Service Locator. */
    private val repo = ServiceLocator.repo
    /** Repositorio local para la lógica de filtros. */
    private val filterRepo: FilterRepository

    /** Indicador de operación en curso. */
    private val _loading = MutableStateFlow(true)
    val loading: StateFlow<Boolean> = _loading

    /** Mensaje de error (o `null` si no hay). */
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    /** Lista observable de cupones para la UI. */
    private val _coupons = MutableStateFlow<List<Coupon>>(emptyList())
    val coupons: StateFlow<List<Coupon>> = _coupons

    // --- INICIO: Lógica de Filtros ---

    // Estado que mantiene el conjunto de filtros de categoría activos (ej: {"Restaurante", "Cine"}).
    private val _activeFilters = MutableStateFlow<Set<String>>(emptySet())
    val activeFilters: StateFlow<Set<String>> = _activeFilters

    // Flujo que observa y expone el Top 3 de filtros más usados desde la base de datos.
    val topFilters: StateFlow<List<String>>

    init {
        // Inicializamos el repositorio de filtros con el DAO de nuestra DB persistente.
        val db = DatabaseProvider.get(application.applicationContext)
        filterRepo = FilterRepository(db.filterClickDao())

        // El Top 3 se obtiene de la base de datos y se convierte en un StateFlow.
        topFilters = filterRepo.observeTopThree()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

        // Carga inicial de datos.
        refresh()
    }

    /**
     * Alterna la selección de un filtro de categoría.
     * Si el filtro no estaba activo, lo activa y registra el clic en la base de datos.
     * Si ya estaba activo, lo desactiva.
     *
     * @param type El nombre de la categoría (ej: "Restaurante").
     */
    fun toggleFilter(type: String) {
        viewModelScope.launch {
            val current = _activeFilters.value.toMutableSet()
            if (current.contains(type)) {
                current.remove(type)
            } else {
                current.add(type)
                // Incrementamos el contador solo cuando se selecciona un nuevo filtro.
                filterRepo.incrementFilterClick(type)
            }
            _activeFilters.value = current
        }
    }

    /**
     * Limpia todos los filtros activos, mostrando nuevamente todos los negocios.
     */
    fun clearFilters() {
        _activeFilters.value = emptySet()
    }

    // --- FIN: Lógica de Filtros ---

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
