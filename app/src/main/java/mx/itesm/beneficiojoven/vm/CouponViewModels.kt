package mx.itesm.beneficiojoven.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import mx.itesm.beneficiojoven.model.di.ServiceLocator
import mx.itesm.beneficiojoven.model.Coupon

class CouponListVM : ViewModel() {
    private val repo = ServiceLocator.repo
    private val _loading = MutableStateFlow(true)
    val loading: StateFlow<Boolean> = _loading
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error
    private val _coupons = MutableStateFlow<List<Coupon>>(emptyList())
    val coupons: StateFlow<List<Coupon>> = _coupons

    init { refresh() }
    fun refresh() = viewModelScope.launch {
        _loading.value = true; _error.value = null
        repo.coupons().onSuccess { _coupons.value = it }.onFailure { _error.value = it.message }
        _loading.value = false
    }
}

class CouponDetailVM : ViewModel() {
    private val repo = ServiceLocator.repo
    private val _coupon = MutableStateFlow<Coupon?>(null)
    val coupon: StateFlow<Coupon?> = _coupon
    suspend fun load(id: String) { repo.couponById(id).onSuccess { _coupon.value = it } }
}
