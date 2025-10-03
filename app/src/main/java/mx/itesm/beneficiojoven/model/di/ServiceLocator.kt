package mx.itesm.beneficiojoven.model.di

import mx.itesm.beneficiojoven.model.data.repository.AppRepository
import mx.itesm.beneficiojoven.data.repository.FakeRepository
import mx.itesm.beneficiojoven.model.data.repository.RemoteRepository

object ServiceLocator {
    var useFake: Boolean = true   // cambiar a false cuando tengamos backend
    val repo: AppRepository by lazy { if (useFake) FakeRepository() else RemoteRepository() }
}