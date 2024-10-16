package ucne.edu.ventas.local.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import ucne.edu.ventas.local.data.dao.VentaDao
import ucne.edu.ventas.local.data.entities.VentaEntity
import ucne.edu.ventas.local.remote.dto.VentaDto
import ucne.edu.ventas.local.remote.RemoteDataSource
import ucne.edu.ventas.local.remote.dto.toEntity
import ucne.edu.ventas.utils.Resource
import javax.inject.Inject

class VentaRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val ventaDao: VentaDao
) {
    private suspend fun saveVentaLocal(venta: VentaEntity) = ventaDao.save(venta)
    private fun getVentasLocal() = ventaDao.getVentas()
    fun getVentas(): Flow<Resource<List<VentaEntity>>> = flow{
        try{
            emit(Resource.Loading())
            val ventas =  remoteDataSource.getVentas()
            ventas.forEach{ventaDto ->
                saveVentaLocal(ventaDto.toEntity())
            }
            val ventasLocal = getVentasLocal().firstOrNull()
            if (ventasLocal.isNullOrEmpty()) {
                emit(Resource.Error("No se encontraron ventas"))
            } else {
                emit(Resource.Success(ventasLocal))
            }
        }catch (e: HttpException){
            emit(Resource.Error("Error de internet ${e.message}"))
        } catch (e: Exception) {
            emit(Resource.Error("Unknown error: ${e.message}"))
        }
    }
}