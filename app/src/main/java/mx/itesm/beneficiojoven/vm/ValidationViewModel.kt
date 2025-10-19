package mx.itesm.beneficiojoven.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import mx.itesm.beneficiojoven.model.Coupon
import mx.itesm.beneficiojoven.model.di.ServiceLocator

class ValidationViewModel : ViewModel() {

    private val repository = ServiceLocator.repo

    private val _validatedCoupon = MutableStateFlow<Coupon?>(null)
    val validatedCoupon = _validatedCoupon.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    fun validateCoupon(code: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            _validatedCoupon.value = null // Limpiar resultado anterior

            repository.validateCoupon(code)
                .onSuccess { coupon ->
                    _validatedCoupon.value = coupon
                }
                .onFailure { throwable ->
                    _error.value = throwable.message ?: "Código de cupón no válido o expirado."
                }
            _isLoading.value = false
        }
    }

    fun clear() {
        _validatedCoupon.value = null
        _error.value = null
    }
}
