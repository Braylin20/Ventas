package ucne.edu.ventas.local.repository

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import org.junit.Assert.*

import org.junit.Test
import ucne.edu.ventas.local.remote.dto.VentaDto
import kotlinx.coroutines.test.runTest
import ucne.edu.ventas.local.data.dao.VentaDao
import ucne.edu.ventas.local.data.entities.VentaEntity
import ucne.edu.ventas.local.remote.RemoteDataSource
import ucne.edu.ventas.local.remote.dto.toEntity
import ucne.edu.ventas.utils.Resource

class VentaRepositoryTest{

    @Test
    fun getVentas() = runTest{
        //given
        val ventasRemote = listOf(
            VentaDto(1, "Enmanuel", 2.2, 2.2, 2.2,2.2,2.2),
            VentaDto(2, "Manuel", 2.2, 2.2, 2.2,2.2,2.2),
            VentaDto(3, "Rafael", 2.2, 2.2, 2.2,2.2,2.2),
        )
        val ventasLocal= ventasRemote.map{
            it.toEntity()
        }

        val remoteDataSource = mockk<RemoteDataSource>()
        val ventaDao = mockk<VentaDao>()
        val ventaRepository = VentaRepository(remoteDataSource, ventaDao)

        coEvery { remoteDataSource.getVentas() }returns ventasRemote
        coEvery { ventaDao.save(any()) }returns Unit
        coEvery { ventaDao.getVentas() }returns flow { emit(ventasLocal) }

        //act
        val result = ventaRepository.getVentas().first()

        //assert
        assert(result is Resource.Success)
        assert((result as Resource.Success).data==ventasLocal)
        coVerify { ventaDao.save(any()) }
    }
}