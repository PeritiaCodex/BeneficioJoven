package mx.itesm.beneficiojoven.model.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad Room para rastrear los clics en los filtros de categorías.
 *
 * Almacena el conteo de cuántas veces se ha seleccionado cada tipo de filtro.
 *
 * @property type El nombre de la categoría (p. ej., "Comida", "Salud"). Es la clave primaria.
 * @property clickCount El número total de veces que se ha hecho clic en este filtro.
 */
@Entity(tableName = "filter_clicks")
data class FilterClickEntity(
    @PrimaryKey val type: String,
    val clickCount: Int
)