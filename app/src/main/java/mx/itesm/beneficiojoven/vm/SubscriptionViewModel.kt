package mx.itesm.beneficiojoven.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import mx.itesm.beneficiojoven.model.data.repository.AppRepository
import mx.itesm.beneficiojoven.model.di.ServiceLocator

class SubscriptionViewModel(
    private val repo: AppRepository = ServiceLocator.repo
) : ViewModel() {

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun toggleSubscription(merchantId: String) = viewModelScope.launch {
        _loading.value = true
        _error.value = null
        repo.toggleSubscription(merchantId)
            .onFailure { _error.value = it.message }
        _loading.value = false
    }

    fun clearError() {
        _error.value = null
    }
}