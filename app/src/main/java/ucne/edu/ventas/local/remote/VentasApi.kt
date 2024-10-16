package ucne.edu.ventas.local.remote

import kotlinx.coroutines.flow.Flow
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import ucne.edu.ventas.local.remote.dto.VentaDto

interface VentasApi {
    @GET("api/Ventas")
    suspend fun getVentas(): List<VentaDto>
    @POST("api/Ventas")
    suspend fun addVentas(@Body ventasDto: VentaDto): VentaDto
}