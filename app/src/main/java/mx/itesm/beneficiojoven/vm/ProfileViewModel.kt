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
 * ViewModel para la pantalla de **Perfil**.
 *
 * Se encarga de cargar los datos del perfil del usuario autenticado, como el nombre
 * completo, correo y municipio, desde el repositorio. Expone el estado de la carga,
 * los datos del perfil y posibles errores a través de `StateFlow`.
 *
 * @see ServiceLocator
 * @see Session
 */
class ProfileViewModel : ViewModel() {
    private val repository = ServiceLocator.repo

    /** @property _profile Flujo mutable privado que contiene el [User] con los datos del perfil. */
    private val _profile = MutableStateFlow<User?>(null)
    /** @property profile Flujo de solo lectura que expone los datos del perfil del usuario. */
    val profile = _profile.asStateFlow()

    /** @property _loading Flujo mutable privado para el estado de carga. */
    private val _loading = MutableStateFlow(false)
    /** @property loading Flujo de solo lectura que indica si la operación de carga del perfil está en curso. */
    val loading = _loading.asStateFlow()

    /** @property _error Flujo mutable privado para los mensajes de error. */
    private val _error = MutableStateFlow<String?>(null)
    /** @property error Flujo de solo lectura que expone el mensaje de error si la carga falla. */
    val error = _error.asStateFlow()

    /**
     * Carga los datos del perfil del usuario actual.
     *
     * Obtiene el `userId` de la [Session] actual, llama al repositorio para
     * obtener los datos del perfil y actualiza los flujos `profile`, `loading` y `error`
     * según el resultado de la operación.
     */
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
