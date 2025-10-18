package mx.itesm.beneficiojoven.vm

import android.app.Application
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import mx.itesm.beneficiojoven.model.UserProfile
import mx.itesm.beneficiojoven.model.data.repository.AppRepository
import mx.itesm.beneficiojoven.model.data.repository.RemoteRepository

/** DataStore de la aplicación para preferencias simples (scope de la app). */
private val Application.dataStore by preferencesDataStore("settings")

/**
 * ViewModel de **Perfil** orientado a gestionar preferencias del usuario
 * y cargar los datos del perfil desde el repositorio.
 */
class ProfileViewModel(
    app: Application,
    private val repo: AppRepository = RemoteRepository()
) : AndroidViewModel(app) {

    // --- State para Datos del Perfil ---
    private val _profile = MutableStateFlow<UserProfile?>(null)
    val profile: StateFlow<UserProfile?> = _profile

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    /**
     * Carga los datos del perfil del usuario desde el repositorio.
     * Actualiza los `StateFlow`s de loading, error y profile.
     */
    fun loadProfile() {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            repo.getProfile()
                .onSuccess { _profile.value = it }
                .onFailure { _error.value = it.message }
            _loading.value = false
        }
    }


    // --- State para Preferencias (DataStore) ---
    private val key = booleanPreferencesKey("push_enabled")

    val pushEnabled = app.dataStore.data.map { it[key] ?: true }

    suspend fun setPush(enabled: Boolean) {
        getApplication<Application>().dataStore.edit { it[key] = enabled }
    }
}
