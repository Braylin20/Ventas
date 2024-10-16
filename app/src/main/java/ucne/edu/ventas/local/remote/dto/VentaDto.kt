package ucne.edu.ventas.local.remote.dto

import ucne.edu.ventas.local.data.entities.VentaEntity

data class VentaDto (
    val ventaId: Int,
    val cliente: String,
    val precio: Double,
    val totalGalones: Double,
    val descuento: Double,
    val totalDescuento: Double,
    val total: Double,
)

fun VentaDto.toEntity(): VentaEntity {
    return VentaEntity(
        ventaId = ventaId,
        cliente = cliente,
        precio = precio,
        totalGalones = totalGalones,
        descuento = descuento,
        totalDescuento = totalDescuento,
        total = total
    )
}