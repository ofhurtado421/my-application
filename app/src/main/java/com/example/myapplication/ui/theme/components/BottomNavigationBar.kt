package com.example.myapplication.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Contacts
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.myapplication.ui.navigation.AppScreens

/**
 * Modelo de datos para cada ítem de la barra de navegación inferior.
 * @param label Texto que aparece debajo del ícono.
 * @param icon Ícono de Material Design.
 * @param route Ruta de navegación asociada.
 */
data class BottomNavItem(
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val route: String
)

/**
 * Barra de navegación inferior con 3 módulos principales.
 * Resalta automáticamente el ítem de la pantalla actual.
 * Al tocar un ítem navega a la pantalla correspondiente.
 *
 * @param navController Controlador de navegación para manejar rutas.
 */
@Composable
fun BottomNavigationBar(navController: NavController) {

    // Lista de ítems de la barra de navegación
    val items = listOf(
        BottomNavItem(
            label = "Clientes",
            icon = Icons.Filled.Contacts,
            route = AppScreens.ContactList.route
        ),
        BottomNavItem(
            label = "Productos",
            icon = Icons.Filled.Inventory,
            route = AppScreens.ProductList.route
        ),
        BottomNavItem(
            label = "Proveedores",
            icon = Icons.Filled.LocalShipping,
            route = AppScreens.ProviderList.route
        )
    )

    /**
     * Obtiene la ruta actual de navegación para
     * resaltar el ítem correcto en la barra.
     */
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                /**
                 * selected: compara la ruta actual con la del ítem
                 * para saber cuál debe estar resaltado.
                 */
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        /**
                         * popUpTo evita acumular pantallas en el backstack
                         * al navegar entre los módulos principales.
                         * Esto evita que al presionar "atrás" el usuario
                         * tenga que pasar por todas las pantallas visitadas.
                         */
                        popUpTo(AppScreens.Home.route) {
                            saveState = true // guarda el estado de la pantalla
                        }
                        launchSingleTop = true  // evita duplicar la pantalla
                        restoreState = true     // restaura el estado guardado
                    }
                },
                icon = { Icon(imageVector = item.icon, contentDescription = item.label) },
                label = { Text(text = item.label) }
            )
        }
    }
}
