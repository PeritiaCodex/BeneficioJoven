package mx.itesm.beneficiojoven.vm

import android.app.Application
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.map

private val Application.dataStore by preferencesDataStore("settings")

class ProfileViewModel(app: Application) : AndroidViewModel(app) {
    private val key = booleanPreferencesKey("push_enabled")
    val pushEnabled = app.dataStore.data.map { it[key] ?: true }
    suspend fun setPush(enabled: Boolean) {
        getApplication<Application>().dataStore.edit { it[key] = enabled }
    }
}
