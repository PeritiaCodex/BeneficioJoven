package mx.itesm.beneficiojoven.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import mx.itesm.beneficiojoven.model.data.repository.AppRepository
import mx.itesm.beneficiojoven.model.di.ServiceLocator

class SubscriptionViewModel(
    private val repo: AppRepository = ServiceLocator.repo
) : ViewModel() {

    // --- State for the UI ---
    private val _isSubscribed = MutableStateFlow<Boolean?>(null) // null = loading, true/false = state
    val isSubscribed: StateFlow<Boolean?> = _isSubscribed.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    /**
     * Comprueba el estado de suscripción inicial para un comercio específico.
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

    fun clearError() {
        _error.value = null
    }
}
