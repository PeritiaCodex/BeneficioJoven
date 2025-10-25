package mx.itesm.beneficiojoven.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import mx.itesm.beneficiojoven.model.Coupon
import mx.itesm.beneficiojoven.model.di.ServiceLocator

/**
 * ViewModel para la pantalla de **Validación de Cupones**.
 *
 * Se encarga de la lógica para validar un código de cupón a través del repositorio.
 * Expone el resultado de la validación, el estado de carga y posibles errores
 * a la UI a través de `StateFlow`.
 *
 * @property repository Repositorio de datos obtenido desde [ServiceLocator].
 */
class ValidationViewModel : ViewModel() {

    private val repository = ServiceLocator.repo

    /** @property _validatedCoupon Flujo mutable privado que contiene el [Coupon] validado. */
    private val _validatedCoupon = MutableStateFlow<Coupon?>(null)
    /** @property validatedCoupon Flujo de solo lectura que expone el cupón si la validación fue exitosa. */
    val validatedCoupon = _validatedCoupon.asStateFlow()

    /** @property _isLoading Flujo mutable privado para el estado de carga. */
    private val _isLoading = MutableStateFlow(false)
    /** @property isLoading Flujo de solo lectura que indica si la operación de validación está en curso. */
    val isLoading = _isLoading.asStateFlow()

    /** @property _error Flujo mutable privado para los mensajes de error. */
    private val _error = MutableStateFlow<String?>(null)
    /** @property error Flujo de solo lectura que expone el mensaje de error si la validación falla. */
    val error = _error.asStateFlow()

    /**
     * Valida un cupón a partir de su código.
     *
     * Lanza una corutina para llamar al repositorio y actualiza los flujos
     * `validatedCoupon`, `isLoading` y `error` según el resultado.
     *
     * @param code El código del cupón a validar.
     */
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

    /**
     * Limpia los estados de `validatedCoupon` y `error`.
     *
     * Debe ser llamado por la UI después de mostrar un resultado para
     * preparar el ViewModel para una nueva validación.
     */
    fun clear() {
        _validatedCoupon.value = null
        _error.value = null
    }
}
