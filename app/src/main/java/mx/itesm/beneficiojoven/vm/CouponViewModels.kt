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
 * - Carga cupones y la lista de comercios desde [ServiceLocator].
 * - Gestiona estados de carga, error y la lista de cupones.
 * - Mantiene un mapa para resolver los IDs de los comercios.
 *
 * @param application Requerido por AndroidViewModel para obtener el contexto.
 */
class CouponListVM(application: Application) : AndroidViewModel(application) {
    /** Repositorio remoto obtenido desde el Service Locator. */
    private val repo = ServiceLocator.repo
    /** Repositorio local para la lógica de filtros. */
    private val filterRepo: FilterRepository

    /** @property _loading Indicador de operación en curso (privado). */
    private val _loading = MutableStateFlow(true)
    /** @property loading Flujo de solo lectura que expone el estado de carga. */
    val loading: StateFlow<Boolean> = _loading

    /** @property _error Mensaje de error (privado, `null` si no hay). */
    private val _error = MutableStateFlow<String?>(null)
    /** @property error Flujo de solo lectura que expone el mensaje de error. */
    val error: StateFlow<String?> = _error

    /** @property _coupons Lista observable de cupones para la UI (privada). */
    private val _coupons = MutableStateFlow<List<Coupon>>(emptyList())
    /** @property coupons Flujo de solo lectura que expone la lista de cupones. */
    val coupons: StateFlow<List<Coupon>> = _coupons

    /** @property _merchantIdMap Mapa para buscar IDs numéricos de comercios a partir de su nombre (privado). */
    private val _merchantIdMap = MutableStateFlow<Map<String, Int>>(emptyMap())
    /** @property merchantIdMap Flujo de solo lectura que expone el mapa de IDs de comercios. */
    val merchantIdMap: StateFlow<Map<String, Int>> = _merchantIdMap

    // --- INICIO: Lógica de Filtros ---

    /** @property _activeFilters Conjunto de filtros de tipo de comercio actualmente activos (privado). */
    private val _activeFilters = MutableStateFlow<Set<String>>(emptySet())
    /** @property activeFilters Flujo de solo lectura que expone los filtros activos. */
    val activeFilters: StateFlow<Set<String>> = _activeFilters

    /** @property topFilters Flujo que expone los 3 filtros más utilizados por el usuario. */
    val topFilters: StateFlow<List<String>>

    init {
        val db = DatabaseProvider.get(application.applicationContext)
        filterRepo = FilterRepository(db.filterClickDao())

        topFilters = filterRepo.observeTopThree()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

        refresh()
    }

    /**
     * Activa o desactiva un filtro por tipo de comercio.
     *
     * Si el filtro ya está activo, lo elimina. Si no, lo añade al conjunto de filtros
     * activos y registra el clic para las estadísticas de filtros populares.
     *
     * @param type El tipo de comercio a filtrar (ej. "Restaurante").
     */
    fun toggleFilter(type: String) {
        viewModelScope.launch {
            val current = _activeFilters.value.toMutableSet()
            if (current.contains(type)) {
                current.remove(type)
            } else {
                current.add(type)
                filterRepo.incrementFilterClick(type)
            }
            _activeFilters.value = current
        }
    }

    /**
     * Limpia todos los filtros activos, mostrando nuevamente todos los cupones.
     */
    fun clearFilters() {
        _activeFilters.value = emptySet()
    }

    // --- FIN: Lógica de Filtros ---

    /**
     * Vuelve a solicitar la lista de cupones y de comercios al repositorio.
     *
     * Actualiza los flujos de `loading`, `error`, `coupons` y `merchantIdMap`.
     * Las llamadas de red para cupones y comercios se ejecutan en paralelo.
     */
    fun refresh() = viewModelScope.launch {
        _loading.value = true
        _error.value = null

        // Lanzamos ambas llamadas de red en paralelo para eficiencia
        val couponsResult = repo.coupons()
        val merchantsResult = repo.listMerchants()

        // Procesamos el resultado de los cupones
        couponsResult.onSuccess {
            _coupons.value = it
        }.onFailure {
            _error.value = it.message
            _loading.value = false
            return@launch // Si los cupones fallan, no continuamos
        }

        // Procesamos el resultado de los comercios
        merchantsResult.onSuccess { merchantProfiles ->
            // Creamos un mapa de merchant_name -> user_id
            _merchantIdMap.value = merchantProfiles.associate {
                it.merchant_name to it.user_id
            }
        }.onFailure {
            // Un fallo aquí no es crítico, la app funciona pero sin suscripciones.
            // Podemos registrar el error, pero no lo mostramos al usuario para no ser intrusivos.
            println("No se pudo cargar el directorio de comercios: ${it.message}")
        }

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

    /** @property _coupon Cupón actual a mostrar en detalle (privado, o `null`). */
    private val _coupon = MutableStateFlow<Coupon?>(null)
    /** @property coupon Flujo de solo lectura que expone el cupón cargado. */
    val coupon: StateFlow<Coupon?> = _coupon

    /**
     * Carga un cupón por su identificador y lo publica en [coupon].
     *
     * @param id Identificador del cupón (como `String`).
     */
    suspend fun load(id: String) { repo.couponById(id).onSuccess { _coupon.value = it } }
}
