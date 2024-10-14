package ucne.edu.ventas.local.data.remote.dto

class VentaDto (
    val ventaId: Int,
    val cliente: String,
    val precio: Double,
    val totalGalones: Double,
    val descuento: Double,
    val totalDescuento: Double,
    val total: Double,
)