package mx.itesm.beneficiojoven.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import mx.itesm.beneficiojoven.model.di.ServiceLocator
import mx.itesm.beneficiojoven.model.User

class AuthViewModel : ViewModel() {
    private val repo = ServiceLocator.repo
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun login(email: String, password: String) = viewModelScope.launch {
        _loading.value = true; _error.value = null
        repo.login(email, password).onSuccess { _user.value = it }
            .onFailure { _error.value = it.message }
        _loading.value = false
    }
}