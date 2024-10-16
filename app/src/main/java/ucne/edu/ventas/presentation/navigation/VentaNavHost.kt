package ucne.edu.ventas.presentation.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import edu.ucne.braylinvasquez_p1_ap2.presentation.navigation.algo.VentaListScreen

@Composable
fun VentaNavHost(
    navHostController: NavHostController
) {
    NavHost(
        navController = navHostController,
        startDestination = Screen.VentaListScreen
    ){
        composable<Screen.VentaListScreen> {
            VentaListScreen(
                onGoCreate = { navHostController.navigate(Screen.VentaScreen(0)) },
                onGoEdit = {navHostController.navigate(Screen.VentaScreen(it))}
            )
        }
    }
}