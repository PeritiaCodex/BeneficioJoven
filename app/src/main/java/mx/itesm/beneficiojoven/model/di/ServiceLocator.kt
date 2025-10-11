package mx.itesm.beneficiojoven.model.di

import mx.itesm.beneficiojoven.model.data.repository.AppRepository
import mx.itesm.beneficiojoven.model.data.repository.RemoteRepository

object ServiceLocator {
    var useFake: Boolean = false   // cambiar a false cuando tengamos backend
    val repo: AppRepository by lazy { if (useFake) RemoteRepository() else RemoteRepository() }
}