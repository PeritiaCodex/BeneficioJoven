package mx.itesm.beneficiojoven.data.repository

import kotlinx.coroutines.delay
import mx.itesm.beneficiojoven.model.Coupon
import mx.itesm.beneficiojoven.model.Merchant
import mx.itesm.beneficiojoven.model.User
import mx.itesm.beneficiojoven.model.data.repository.AppRepository

class FakeRepository : AppRepository {
    private val tienda = Merchant("m1", "Cafetería Campus", null, "Food")
    private val tienda2 = Merchant("m2", "Librería Tec", null, "Books")

    private val data = listOf(
        Coupon(
            "c1",
            "Latte 2x1",
            "Promoción válida todo septiembre",
            null,
            "2x1",
            tienda,
            "2025-09-30",
            null
        ),
        Coupon(
            "c2",
            "10% en libros",
            "Aplica en títulos seleccionados",
            null,
            "10% OFF",
            tienda2,
            "2025-10-15",
            null
        ),
        Coupon(
            "c3",
            "Sándwich $59",
            "Combo sándwich + bebida",
            null,
            "$59 MXN",
            tienda,
            "2025-09-28",
            null
        ),
    )

    override suspend fun login(email: String, password: String) = runCatching {
        delay(600); User("u1", email, "Juan Estudiante")
    }

    override suspend fun register(name: String, email: String, password: String) = runCatching {
        delay(800); User("u2", email, name)
    }

    override suspend fun coupons() = runCatching {
        delay(500); data
    }

    override suspend fun couponById(id: String) = runCatching {
        delay(300); data.first { it.id == id }
    }
}
