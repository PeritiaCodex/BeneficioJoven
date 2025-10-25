package mx.itesm.beneficiojoven.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import mx.itesm.beneficiojoven.model.data.repository.AppRepository
import mx.itesm.beneficiojoven.model.di.ServiceLocator

/**
 * ViewModel para gestionar el estado de suscripción de un **comercio individual**.
 *
 * Este ViewModel se encarga de:
 * - Verificar si el usuario está suscrito a un comercio específico (`checkInitialSubscription`).
 * - Alternar el estado de la suscripción (`toggleSubscription`).
 * - Exponer los estados de UI relevantes: `isSubscribed`, `loading`, y `error`.
 *
 * @param repo El repositorio de la aplicación, inyectado a través de [ServiceLocator].
 */
class SubscriptionViewModel(
    private val repo: AppRepository = ServiceLocator.repo
) : ViewModel() {

    /** Estado de la suscripción. `null` indica estado inicial/de carga, `true` suscrito, `false` no suscrito. */
    private val _isSubscribed = MutableStateFlow<Boolean?>(null)
    val isSubscribed: StateFlow<Boolean?> = _isSubscribed.asStateFlow()

    /** Indica si una operación de red (suscripción/desuscripción) está en curso. */
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    /** Contiene el mensaje de error de la última operación fallida. `null` si no hay error. */
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    /**
     * Comprueba el estado de suscripción inicial para un comercio específico.
     *
     * Obtiene la lista de todos los IDs de comercios suscritos y verifica si el `merchantId`
     * actual está presente, actualizando el estado `isSubscribed`.
     *
     * @param merchantId El ID del comercio a verificar.
     */
    fun checkInitialSubscription(merchantId: String) {
        viewModelScope.launch {
            repo.getSubscribedMerchants()
                .onSuccess { subscribedIds ->
                    try {
                        val currentId = merchantId.toInt()
                        _isSubscribed.value = subscribedIds.contains(currentId)
                    } catch (e: NumberFormatException) {
                        _isSubscribed.value = false // Asumir no suscrito si el ID no es numérico
                    }
                }
                .onFailure {
                    _error.value = "No se pudo verificar la suscripción."
                    _isSubscribed.value = false // Asumir no suscrito en caso de error
                }
        }
    }

    /**
     * Alterna el estado de suscripción y actualiza la UI con la respuesta del servidor.
     *
     * Llama al repositorio para cambiar la suscripción y actualiza `isSubscribed` con el
     * nuevo estado devuelto por el backend.
     *
     * @param merchantId El ID del comercio cuya suscripción se va a alternar.
     */
    fun toggleSubscription(merchantId: String) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null

            repo.toggleSubscription(merchantId)
                .onSuccess { response ->
                    _isSubscribed.value = response.subscribed
                }
                .onFailure { throwable ->
                    _error.value = throwable.message
                    // El estado de la UI será revertido por el `LaunchedEffect` que observa el error.
                }
            _loading.value = false
        }
    }

    /**
     * Limpia el estado de error para que la UI no muestre errores antiguos.
     */
    fun clearError() {
        _error.value = null
    }
}
