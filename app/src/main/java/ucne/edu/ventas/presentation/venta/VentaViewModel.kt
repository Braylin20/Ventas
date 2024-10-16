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


    data class UiState(
        val ventaId: Int? = null,
        val cliente: String? = null,
        val precio: Double? = null,
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