package ucne.edu.ventas.presentation.venta

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ucne.edu.ventas.ui.theme.VentasTheme
import java.util.Locale

@Composable
fun VentaCreateScreen(
    viewModel: VentaViewModel = hiltViewModel(),
    onBack: () -> Unit,
    ventaId: Int
) {
//    LaunchedEffect(key1 = ventaId) {
//        if(ventaId>0){
//            viewModel.selectedVenta(ventaId)
//        }
//    }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    VentaCreateBodyScreen(
        uiState = uiState,
        onBack = onBack,
        save = viewModel::addVenta,
        onClienteChange = viewModel::onClienteChange,
        onPrecioChange = viewModel::onPrecioChange,
        onCantidadGalonesChange = viewModel::onCantidadGalonesChange,
        onDescuentoChange = viewModel::onDescuentoChange,

        onTotalChange = viewModel::onTotalChange,
        ventaId = ventaId
    )
}


@Composable
fun VentaCreateBodyScreen(
    uiState: VentaViewModel.UiState,
    onBack: () -> Unit,
    save: () -> Unit,
    onClienteChange: (String) -> Unit,
    onPrecioChange: (Double) -> Unit,
    onCantidadGalonesChange: (Double) -> Unit,
    onDescuentoChange: (Double) -> Unit,
    onTotalChange: () -> Unit,
    ventaId: Int
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onBack
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Save"
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Registro de Venta",
                    fontSize = 28.sp
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                OutlinedTextField(
                    value = uiState.cliente?:"",
                    onValueChange = onClienteChange,
                    label = { Text("Cliente") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = uiState.message != null && uiState.message == "Cliente no puede estar vacío",
                )
                if (uiState.clienteError != null) {
                    Text(
                        text = uiState.clienteError,
                        color = Color.Red,
                        fontSize = 14.sp
                    )
                }
                OutlinedTextField(
                    value = uiState.totalGalones?.toString()?:"",
                    onValueChange = {onCantidadGalonesChange(it.toDouble())},
                    label = { Text("Cantidad de Galones") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                )
                if (uiState.totalGalonesError != null) {
                    Text(
                        text = uiState.totalGalonesError,
                        color = Color.Red,
                        fontSize = 14.sp
                    )
                }
                OutlinedTextField(
                    value = uiState.descuento?.toString()?:"",
                    onValueChange = {onDescuentoChange(it.toDouble())},
                    label = { Text("Descuento") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(
                    label = { Text("Precio") },
                    value = uiState.precio?.toString()?:"",
                    onValueChange = {onPrecioChange(it.toDouble())},
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true
                )
                OutlinedTextField(
                    value = uiState.totalDescuento?.toString()?:"",
                    onValueChange = {},
                    label = { Text("Total Descontado") },
                    modifier = Modifier.fillMaxWidth()
                )
                if (uiState.totalDescuentoError != null) {
                    Text(
                        text = uiState.totalDescuentoError,
                        color = Color.Red,
                        fontSize = 14.sp
                    )
                }
                OutlinedTextField(
                    value = String.format(Locale.getDefault(), "%.2f", uiState.total ?: 0.0),
                    onValueChange = {onTotalChange()},
                    label = { Text("Total") },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true
                )
                if (uiState.totalError != null) {
                    Text(
                        text = uiState.totalError,
                        color = Color.Red,
                        fontSize = 14.sp
                    )
                }
                Text(text = uiState.message ?: "", color = Color.Green, fontSize = 14.sp)
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            ){
                OutlinedButton(
                    onClick = save
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Save")
                    Text("Guardar")
                }
            }
        }

    }
}


@Preview(showSystemUi = true)
@Composable
private fun VentaScreenPreview() {
    VentasTheme  {
        VentaCreateBodyScreen(
            uiState = VentaViewModel.UiState(),
            onBack = {},
            save = {},
            onClienteChange = {},
            onPrecioChange = {},
            onCantidadGalonesChange = {},
            onDescuentoChange = {},
            onTotalChange = {},
            ventaId = 0
        )
    }

}