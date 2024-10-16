package ucne.edu.ventas.presentation.navigation

import kotlinx.serialization.Serializable

sealed class Screen {
    @Serializable
    data class VentaScreen(val id: Int) : Screen()
    @Serializable
    data object VentaListScreen : Screen()
}