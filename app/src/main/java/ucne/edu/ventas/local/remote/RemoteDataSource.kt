package ucne.edu.ventas.local.remote

import ucne.edu.ventas.local.remote.dto.VentaDto
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val ventasApi: VentasApi
) {
    suspend fun getVentas() = ventasApi.getVentas()
    suspend fun addVenta(ventaDto: VentaDto) = ventasApi.addVentas(ventaDto)
}