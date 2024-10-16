package ucne.edu.ventas.local.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Ventas")
data class VentaEntity (
    @PrimaryKey
    val ventaId: Int? = null,
    val cliente: String? = "",
    val precio: Double? = null,
    val totalGalones: Double? = null,
    val descuento: Double? = null,
    val totalDescuento: Double? = null,
    val total: Double? = null,
)