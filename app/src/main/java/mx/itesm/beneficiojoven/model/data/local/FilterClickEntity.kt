package mx.itesm.beneficiojoven.model.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "filter_clicks")
data class FilterClickEntity(
    @PrimaryKey val type: String,
    val clickCount: Int
)