package ucne.edu.ventas.local.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import ucne.edu.ventas.local.data.entities.VentaEntity

@Dao
interface VentaDao {
    @Upsert
    suspend fun save(venta: VentaEntity)

    @Delete
    suspend fun delete(venta: VentaEntity)

    @Query("SELECT * FROM Ventas WHERE ventaId = :ventaId LIMIT 1")
    suspend fun find(ventaId: Int): VentaEntity?

    @Query("SELECT * From Ventas")
    fun getVentas(): Flow<List<VentaEntity>>
}