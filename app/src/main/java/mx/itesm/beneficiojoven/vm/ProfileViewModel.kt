package mx.itesm.beneficiojoven.vm

import android.app.Application
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import mx.itesm.beneficiojoven.model.User
import mx.itesm.beneficiojoven.model.data.remote.Session
import mx.itesm.beneficiojoven.model.di.ServiceLocator
import android.util.Log

/** DataStore de la aplicación para preferencias simples (scope de la app). */
private val Application.dataStore by preferencesDataStore("settings")

/**
 * ViewModel de **Perfil** orientado a gestionar preferencias del usuario
 * almacenadas en **DataStore Preferences** (por ejemplo, si las notificaciones push están habilitadas).
 *
 * @constructor Crea un [ProfileViewModel] con acceso a [Application] para utilizar DataStore.
 * @see preferencesDataStore
 */
class ProfileViewModel : ViewModel() {
    private val repository = ServiceLocator.repo

    private val _profile = MutableStateFlow<User?>(null)
    val profile = _profile.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    fun loadProfile() {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            // Obtenemos el ID del usuario de la sesión
            val userId = Session.userId?.toString()

            Log.d("DEBUG_PROFILE", "ID leído de la Sesión: $userId")

            if (userId == null) {
                _error.value = "No se pudo encontrar el ID del usuario."
                _loading.value = false
                return@launch
            }
            repository.getProfile(userId)
                .onSuccess { user ->
                    _profile.value = user
                }
                .onFailure { throwable ->
                    _error.value = throwable.message ?: "Error al cargar el perfil"
                }
            _loading.value = false
        }
    }
}