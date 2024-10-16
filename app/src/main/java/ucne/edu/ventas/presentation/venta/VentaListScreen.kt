package edu.ucne.braylinvasquez_p1_ap2.presentation.navigation.algo

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.delay
import ucne.edu.ventas.local.data.entities.VentaEntity
import ucne.edu.ventas.local.remote.dto.VentaDto
import ucne.edu.ventas.presentation.venta.VentaViewModel
import ucne.edu.ventas.ui.theme.VentasTheme


@Composable
fun VentaListScreen(
    viewModel: VentaViewModel = hiltViewModel(),
    onGoCreate: () -> Unit,
    onGoEdit: (Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    VentaListBodyScreen(
        uiState = uiState,
        onGoCreate = onGoCreate,
        onGoEdit = onGoEdit
    )
}


@Composable
fun VentaListBodyScreen(
    uiState: VentaViewModel.UiState,
    onGoCreate: () -> Unit,
    onGoEdit: (Int) -> Unit
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onGoCreate
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add"
                )
            }
        }
    ) {innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
        ) {
            Text(
                text = "Lista de Ventas",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.CenterHorizontally)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp),
                horizontalArrangement = Arrangement.Center
            ){
                Text(
                    text = "Id",
                    modifier = Modifier.weight(0.5f)
                )
                Text(
                    text = "Cliente",
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "Galones",
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text ="Precio",
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "Descuento",
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "Total",
                    modifier = Modifier.weight(1f)
                )
            }
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.CenterHorizontally)
                )
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 15.dp),
            ){
                items(
                    uiState.ventas,
                ){
                    VentaListRowScreen(it,onGoEdit)
                }
            }
        }
    }

}


@Composable
fun VentaListRowScreen(
    venta: VentaEntity,
    onGoEdit: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onGoEdit(venta.ventaId?:0)
            }
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)

    ){
        Text(
            text = venta.ventaId.toString(),
            modifier = Modifier.weight(0.5f)
        )
        Text(
            text = venta.cliente?:"",
            modifier = Modifier.weight(1.5f)
        )
        Text(
            text = venta.totalGalones.toString(),
            modifier = Modifier.weight(1f)
        )
        Text(
            text = venta.precio.toString(),
            modifier = Modifier.weight(1f)
        )
        Text(
            text = venta.totalDescuento.toString(),
            modifier = Modifier.weight(1f)
        )
        Text(
            text = venta.total.toString(),
            modifier = Modifier.weight(1f)
        )
    }
}
@Composable
fun <T> SwipeToDeleteContainer(
    item: T,
    onDelete:(T)-> Unit,
    animationDuration: Int = 500,
    content: @Composable (T) -> Unit
) {
    var isRemoved by remember {
        mutableStateOf(false)
    }
    val state = rememberSwipeToDismissBoxState(
        confirmValueChange = {value->
            if(value == SwipeToDismissBoxValue.EndToStart){
                isRemoved = true
                true
            }else
                false
        }
    )
    LaunchedEffect(key1 = isRemoved){
        if(isRemoved){
            delay(animationDuration.toLong())
            onDelete(item)
        }
    }
    AnimatedVisibility(
        visible = !isRemoved,
        exit = shrinkVertically(
            animationSpec = tween(durationMillis = animationDuration),
            shrinkTowards = Alignment.Top
        ) + fadeOut()
    ) {
        SwipeToDismissBox(
            state = state,
            backgroundContent = {
                DeleteBackground(state)
            },
            content = {content(item)},
        )
    }
}

@Composable
fun DeleteBackground(
    swipeToDismissBoxState: SwipeToDismissBoxState
) {
    val color = if(swipeToDismissBoxState.dismissDirection == SwipeToDismissBoxValue.EndToStart){
        Color.Red
    }else Color.Transparent
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color)
            .padding(16.dp),
        contentAlignment = Alignment.CenterEnd
    ){
        Icon(
            imageVector = Icons.Filled.Delete,
            contentDescription = null,
            tint = Color.White,
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun VentaListScreenPreview() {
    VentasTheme {
        VentaListBodyScreen(
            onGoCreate = {},
            onGoEdit = {},
            uiState = VentaViewModel.UiState(),
        )
    }
}