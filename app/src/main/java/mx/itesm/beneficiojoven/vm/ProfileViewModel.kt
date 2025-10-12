package mx.itesm.beneficiojoven.vm

import android.app.Application
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.map

/** DataStore de la aplicación para preferencias simples (scope de la app). */
private val Application.dataStore by preferencesDataStore("settings")

/**
 * ViewModel de **Perfil** orientado a gestionar preferencias del usuario
 * almacenadas en **DataStore Preferences** (por ejemplo, si las notificaciones push están habilitadas).
 *
 * @constructor Crea un [ProfileViewModel] con acceso a [Application] para utilizar DataStore.
 * @see preferencesDataStore
 */
class ProfileViewModel(app: Application) : AndroidViewModel(app) {

    /** Clave booleana que indica si el usuario habilitó notificaciones push. */
    private val key = booleanPreferencesKey("push_enabled")

    /**
     * Flujo reactivo que emite el estado actual de la preferencia **push_enabled**.
     * Por defecto emite `true` cuando no existe el valor en DataStore.
     */
    val pushEnabled = app.dataStore.data.map { it[key] ?: true }

    /**
     * Actualiza la preferencia **push_enabled** en DataStore.
     *
     * @param enabled `true` para habilitar push; `false` para deshabilitar.
     */
    suspend fun setPush(enabled: Boolean) {
        getApplication<Application>().dataStore.edit { it[key] = enabled }
    }
}
