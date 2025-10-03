package mx.itesm.beneficiojoven.model.data.local

import android.content.Context
import androidx.room.Room

object InMemoryDbProvider {
    @Volatile private var instance: AppDatabase? = null

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