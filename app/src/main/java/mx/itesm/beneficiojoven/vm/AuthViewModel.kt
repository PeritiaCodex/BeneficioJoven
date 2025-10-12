// mx/itesm/beneficiojoven/vm/AuthViewModel.kt
package mx.itesm.beneficiojoven.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import mx.itesm.beneficiojoven.model.User
import mx.itesm.beneficiojoven.model.data.repository.AppRepository
import mx.itesm.beneficiojoven.model.data.repository.RemoteRepository // o tu ServiceLocator

class AuthViewModel(
    private val repo: AppRepository = RemoteRepository()
) : ViewModel() {

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun login(email: String, password: String) = viewModelScope.launch {
        _loading.value = true; _error.value = null
        repo.login(email, password)
            .onSuccess { _user.value = it }
            .onFailure { _error.value = it.message }
        _loading.value = false
    }

    fun register(
        name: String,
        email: String,
        password: String,
        curp: String,
        municipality: String,
        birthDate: String = "2000-01-01"
    ) = viewModelScope.launch {
        _loading.value = true; _error.value = null
        repo.register(name, email, password, curp, municipality, birthDate)
            .onSuccess { _user.value = it }     // ya viene logeado
            .onFailure { _error.value = it.message }
        _loading.value = false
    }

}