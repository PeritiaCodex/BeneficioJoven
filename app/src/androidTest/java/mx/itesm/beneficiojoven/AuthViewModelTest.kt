package mx.itesm.beneficiojoven

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import mx.itesm.beneficiojoven.model.Coupon
import mx.itesm.beneficiojoven.model.Role
import mx.itesm.beneficiojoven.model.User
import mx.itesm.beneficiojoven.model.data.repository.AppRepository
import mx.itesm.beneficiojoven.vm.AuthViewModel
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class AuthViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: AuthViewModel
    private lateinit var fakeRepository: FakeAuthRepository

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        fakeRepository = FakeAuthRepository()
        viewModel = AuthViewModel(repo = fakeRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // --- PRUEBAS DE LOGIN ---

    @Test
    fun login_success_updatesUserAndClearsError() = runTest {
        // 1. Preparación
        fakeRepository.setShouldReturnError(false)
        assertEquals(null, viewModel.user.value)

        // 2. Acción
        viewModel.login("test@user.com", "password123")
        testDispatcher.scheduler.runCurrent() // <-- LÍNEA AÑADIDA

        // 3. Verificación
        assertNotNull(viewModel.user.value)
        assertEquals("1", viewModel.user.value?.id)
        assertNull(viewModel.error.value)
    }

    @Test
    fun login_failure_updatesErrorAndUserIsNull() = runTest {
        // 1. Preparación
        fakeRepository.setShouldReturnError(true)
        assertEquals(null, viewModel.user.value)

        // 2. Acción
        viewModel.login("wrong@user.com", "wrongpassword")
        testDispatcher.scheduler.runCurrent() // <-- LÍNEA AÑADIDA

        // 3. Verificación
        assertNull(viewModel.user.value)
        assertNotNull(viewModel.error.value)
        assertEquals("Error de red simulado", viewModel.error.value)
    }

    // --- PRUEBAS DE REGISTRO ---

    @Test
    fun register_success_updatesUserAndClearsError() = runTest {
        // 1. Preparación
        fakeRepository.setShouldReturnError(false)
        assertEquals(null, viewModel.user.value)

        // 2. Acción
        viewModel.register("New User", "new@user.com", "password123", "CURP123456789", "Municipio Test")
        testDispatcher.scheduler.runCurrent() // <-- LÍNEA AÑADIDA

        // 3. Verificación
        assertNotNull(viewModel.user.value)
        assertEquals("1", viewModel.user.value?.id)
        assertNull(viewModel.error.value)
    }

    @Test
    fun register_failure_updatesErrorAndUserIsNull() = runTest {
        // 1. Preparación
        fakeRepository.setShouldReturnError(true)
        assertEquals(null, viewModel.user.value)

        // 2. Acción
        viewModel.register("Existing User", "existing@user.com", "password123", "CURP_DUPLICADO", "Municipio Test")
        testDispatcher.scheduler.runCurrent() // <-- LÍEA AÑADIDA

        // 3. Verificación
        assertNull(viewModel.user.value)
        assertNotNull(viewModel.error.value)
        assertEquals("CURP ya registrado (simulado)", viewModel.error.value)
    }
}


/**
 * Implementación FALSA de AppRepository para usar en las pruebas.
 * Simula respuestas del servidor sin hacer llamadas de red reales.
 */
class FakeAuthRepository : AppRepository {
    private var shouldReturnError = false
    private val fakeUser = User("1", "test@user.com", "Test User", Role.USER)

    fun setShouldReturnError(value: Boolean) {
        shouldReturnError = value
    }

    override suspend fun login(email: String, password: String): Result<User> {
        return if (shouldReturnError) {
            Result.failure(Exception("Error de red simulado"))
        } else {
            Result.success(fakeUser)
        }
    }

    override suspend fun register(
        name: String,
        email: String,
        password: String,
        curp: String,
        municipality: String,
        birthDate: String
    ): Result<User> {
        return if (shouldReturnError) {
            Result.failure(Exception("CURP ya registrado (simulado)"))
        } else {
            Result.success(fakeUser)
        }
    }

    override suspend fun coupons(): Result<List<Coupon>> {
        TODO("Not yet implemented")
    }

    override suspend fun couponById(id: String): Result<Coupon> {
        TODO("Not yet implemented")
    }
}
