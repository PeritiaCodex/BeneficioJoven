package mx.itesm.beneficiojoven.model.data.local

import android.content.Context
import androidx.room.Room

/**
 * Proveedor **singleton** de una base de datos **en memoria** (Room).
 *
 * Útil para almacenar favoritos durante la sesión sin persistencia en disco.
 * Es *thread-safe* mediante doble verificación con `@Volatile` + `synchronized`.
 */
object InMemoryDbProvider {
    @Volatile private var instance: AppDatabase? = null

    /**
     * Obtiene una instancia única de [AppDatabase] en memoria.
     *
     * @param context Contexto de aplicación.
     * @return Instancia de [AppDatabase] en memoria.
     */
    fun get(context: Context): AppDatabase =
        instance ?: synchronized(this) {
            instance ?: Room.inMemoryDatabaseBuilder(
                context.applicationContext,
                AppDatabase::class.java
            )
                // .allowMainThreadQueries() // evitar; usamos corrutinas
                .build()
                .also { instance = it }
        }
}
