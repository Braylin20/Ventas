package ucne.edu.ventas.presentation.venta

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.w3c.dom.Entity
import ucne.edu.ventas.local.data.entities.VentaEntity
import ucne.edu.ventas.local.remote.dto.VentaDto
import ucne.edu.ventas.local.repository.VentaRepository
import ucne.edu.ventas.utils.Resource
import java.text.DecimalFormat
import javax.inject.Inject

@HiltViewModel
class VentaViewModel @Inject constructor(
    private val ventaRepository: VentaRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    init{
        getVentas()
    }

    private fun getVentas() {
        viewModelScope.launch {
            ventaRepository.getVentas().collectLatest { result ->
                when(result){
                    is Resource.Loading -> {
                        _uiState.update {
                            it.copy(isLoading = true)
                        }
                    }
                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                ventas = result.data?: emptyList(),
                                isLoading = false
                            )
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(isLoading = false)
                        }
                    }
                }
            }
        }
    }

    fun addVenta(){
        viewModelScope.launch {
            val result = ventaRepository.addVenta(uiState.value.toEntity())
            result.collect{ resource->
                when(resource){
                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                message = "Error"
                            )
                        }
                    }
                    is Resource.Loading -> {
                        _uiState.update {
                            it.copy(
                                message = ""
                            )
                        }
                    }
                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                message = "Guardado Correectamente"
                            )
                        }
                    }
                }
            }
        }
    }

    fun onClienteChange(cliente: String) {
        _uiState.update {
            it.copy(
                cliente = cliente,
                clienteError = if(cliente.isNotBlank()) null else "Cliente no puede estar vacío"
            )

        }
    }

    fun onCantidadGalonesChange(totalGalones: Double) {
        _uiState.update {
            it.copy(
                totalGalones = totalGalones,
                totalGalonesError = if(totalGalones > 0) null else "Cantidad de galones no puede estar vacío"
            )
        }
        onTotalDescuentoChange()
        onTotalChange()
    }

    fun onDescuentoChange(descuento: Double) {
        _uiState.update {
            it.copy(
                descuento = descuento,
                descuentoError = if(descuento > 0) null else "Descuento no puede estar vacío"

            )
        }
        onTotalDescuentoChange()
        onTotalChange()
    }

    fun onPrecioChange(precio: Double) {
        val newPrecio = precio.toDouble()
        _uiState.update {
            it.copy(precio = newPrecio)
        }
    }
    private fun onTotalDescuentoChange() {
        val totalDecuento = (uiState.value.totalGalones?: 0.0) *(uiState.value.descuento?: 0.0)
        _uiState.update {
            it.copy(
                totalDescuento = totalDecuento,
            )
        }

    }

    fun onTotalChange() {
        val total = (uiState.value.totalGalones?: 0.0) * (uiState.value.precio!!) - (uiState.value.totalDescuento?:0.0)
        val df = DecimalFormat("#.00")
        val totalFormateado = df.format(total)
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    total = totalFormateado.toDouble(),
                    totalError = if(totalFormateado.toDouble() > 0.0) null else "Total no puede estar vacío"
                )
            }
        }

    }
    private fun nuevo(){
        _uiState.update {
            it.copy(
                ventaId = null,
                cliente = "",
                totalGalones = null,
                descuento = null,
                precio = null,
                totalDescuento = null,
                total = null,
                clienteError = null,
                totalGalonesError = null,
                descuentoError = null,
                precioError = null,
                totalDescuentoError = null,
                totalError = null
            )
        }
    }

    data class UiState(
        val ventaId: Int? = null,
        val cliente: String? = null,
        val precio: Double? = 132.6,
        val totalGalones: Double? = null,
        val descuento: Double? = null,
        val totalDescuento: Double? = null,
        val total: Double? = null,
        val ventas: List<VentaEntity> = emptyList(),
        val message: String? = null,
        val isLoading: Boolean = false,
        val clienteError: String? = null,
        val precioError: String? = null,
        val totalGalonesError: String? = null,
        val descuentoError: String? = null,
        val totalDescuentoError: String? = null,
        val totalError: String? = null,
    )

    fun UiState.toEntity() = VentaDto(
        ventaId= ventaId?:0,
        cliente = cliente?:"",
        precio = precio?:0.0,
        totalGalones= totalGalones?:0.0,
        descuento = descuento?:0.0,
        totalDescuento =totalDescuento?:0.0,
        total = total?:0.0
    )
}