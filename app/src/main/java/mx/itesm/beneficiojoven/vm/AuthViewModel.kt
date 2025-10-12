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

/**
 * ViewModel de **autenticación**: orquesta el flujo de **login** y **registro**,
 * exponiendo estados de carga, error y el [User] autenticado.
 *
 * Usa un [AppRepository] para comunicarse con la capa de datos (por defecto [RemoteRepository]).
 *
 * @constructor Crea un [AuthViewModel] con el repositorio indicado.
 * @see AppRepository
 * @see RemoteRepository
 */
class AuthViewModel(
    private val repo: AppRepository = RemoteRepository()
) : ViewModel() {

    /** Usuario autenticado (o `null` si no hay sesión). */
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    /** Estado de carga para operaciones de autenticación. */
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    /** Mensaje de error de la última operación (o `null` si no hay error). */
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    /**
     * Inicia sesión con correo y contraseña.
     *
     * Actualiza [loading], limpia [error] y, al finalizar:
     * - En éxito, emite el [User] en [user].
     * - En error, emite el mensaje en [error].
     *
     * @param email Correo electrónico.
     * @param password Contraseña en texto plano.
     */
    fun login(email: String, password: String) = viewModelScope.launch {
        _loading.value = true; _error.value = null
        repo.login(email, password)
            .onSuccess { _user.value = it }
            .onFailure { _error.value = it.message }
        _loading.value = false
    }

    /**
     * Registra un usuario y, si el repositorio lo soporta, realiza **auto-login**.
     *
     * Actualiza [loading], limpia [error] y, al finalizar:
     * - En éxito, emite el [User] autenticado en [user].
     * - En error, emite el mensaje en [error].
     *
     * @param name Nombre completo.
     * @param email Correo electrónico.
     * @param password Contraseña en texto plano.
     * @param curp CURP del usuario.
     * @param municipality Municipio de residencia.
     * @param birthDate Fecha de nacimiento con formato `YYYY-MM-DD`. Por defecto `"2000-01-01"`.
     */
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
