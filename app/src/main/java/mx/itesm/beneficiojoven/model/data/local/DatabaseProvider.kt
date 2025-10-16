package mx.itesm.beneficiojoven.model.data.local

import android.content.Context
import androidx.room.Room

/**
 * Proveedor **singleton** de la base de datos **persistente** (Room).
 *
 * Crea un archivo de base de datos en el almacenamiento del dispositivo
 * para que los datos sobrevivan entre sesiones de la app.
 */
object DatabaseProvider {
    @Volatile private var instance: AppDatabase? = null

    /**
     * Obtiene una instancia única de [AppDatabase] persistente.
     *
     * @param context Contexto de aplicación.
     * @return Instancia de [AppDatabase] que guarda en disco.
     */
    fun get(context: Context): AppDatabase =
        instance ?: synchronized(this) {
            instance ?: Room.databaseBuilder( // <-- EL CAMBIO CLAVE
                context.applicationContext,
                AppDatabase::class.java,
                "beneficio_joven.db" // <-- Nombre del archivo de la base de datos
            )
                // Para desarrollo, esto borra y recrea la DB si cambias el esquema.
                // En producción, deberías implementar migraciones.
                .fallbackToDestructiveMigration()
                .build()
                .also { instance = it }
        }
}
    