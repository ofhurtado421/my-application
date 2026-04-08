package com.example.myapplication.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.myapplication.data.entity.AppDatabase
import com.example.myapplication.data.repository.ContactRepository
import com.example.myapplication.data.repository.ProductRepository
import com.example.myapplication.data.repository.ProviderRepository
import com.example.myapplication.data.viewmodel.ContactViewModel
import com.example.myapplication.data.viewmodel.ProductViewModel
import com.example.myapplication.data.viewmodel.ProviderViewModel
import com.example.myapplication.data.viewmodel.factory.ContactViewModelFactory
import com.example.myapplication.data.viewmodel.factory.ProductViewModelFactory
import com.example.myapplication.data.viewmodel.factory.ProviderViewModelFactory
import com.example.myapplication.ui.screens.contact.ContactFormScreen
import com.example.myapplication.ui.screens.contact.ContactListScreen
import com.example.myapplication.ui.screens.home.HomeScreen
import com.example.myapplication.ui.screens.product.ProductFormScreen
import com.example.myapplication.ui.screens.product.ProductListScreen
import com.example.myapplication.ui.screens.provider.ProviderFormScreen
import com.example.myapplication.ui.screens.provider.ProviderListScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.ui.theme.screens.credits.AboutScreen
import com.example.myapplication.ui.theme.screens.splash.SplashScreen


/**
 * Grafo de navegación principal de la aplicación.
 * Conecta todas las pantallas y define cómo se navega entre ellas.
 * También inicializa la base de datos, repositorios y ViewModels
 * que se comparten entre las pantallas.
 *
 * @param navController Controlador que maneja la navegación entre pantallas.
 */
@Composable
fun AppNavGraph(navController: NavHostController) {

    // ─── Base de datos ─────────────────────────────────────
    /**
     * Obtiene el contexto actual para inicializar la base de datos.
     * LocalContext.current es la forma de acceder al contexto en Compose.
     */
    val context = LocalContext.current

    /**
     * Obtiene la instancia única de la base de datos (Singleton).
     * Si ya existe la reutiliza, si no la crea.
     */
    val database = AppDatabase.getInstance(context)

    // ─── Repositorios ──────────────────────────────────────
    /**
     * Crea los repositorios pasándoles los DAOs correspondientes.
     * remember evita recrearlos cada vez que la pantalla se redibuje.
     */
    val contactRepository = ContactRepository(database.contactDao())
    val productRepository = ProductRepository(database.productDao())
    val providerRepository = ProviderRepository(database.providerDao())

    // ─── ViewModels ────────────────────────────────────────
    /**
     * Crea los ViewModels usando sus respectivos Factories.
     * viewModel() de Compose se encarga de que sobrevivan
     * a los cambios de configuración como rotar la pantalla.
     */
    val contactViewModel: ContactViewModel = viewModel(
        factory = ContactViewModelFactory(contactRepository)
    )
    val productViewModel: ProductViewModel = viewModel(
        factory = ProductViewModelFactory(productRepository)
    )
    val providerViewModel: ProviderViewModel = viewModel(
        factory = ProviderViewModelFactory(providerRepository)
    )

    // ─── NavHost ───────────────────────────────────────────
    /**
     * NavHost es el contenedor principal de navegación.
     * startDestination define la primera pantalla que se muestra.
     */
    NavHost(
        navController = navController,
        startDestination = AppScreens.Splash.route
    ) {

        //─── Splash Screen──────────────────────────────────────────


        composable(route = AppScreens.Splash.route) {
            SplashScreen(navController = navController)
        }

        // ─── Home ──────────────────────────────────────────
        /**
         * Pantalla de inicio con acceso a los 3 módulos.
         */
        composable(route = AppScreens.Home.route) {
            HomeScreen(navController = navController)
        }
        // ─── credits──────────────────────────────────────────
        composable(route = AppScreens.About.route) {
            AboutScreen(navController = navController)
        }

        // ─── Contactos ─────────────────────────────────────
        /**
         * Lista de contactos.
         */
        composable(route = AppScreens.ContactList.route) {
            ContactListScreen(
                navController = navController,
                viewModel = contactViewModel
            )
        }

        /**
         * Formulario de contacto.
         * Recibe contactId como argumento:
         *   -1  → modo agregar
         *   >0  → modo editar
         */
        composable(
            route = AppScreens.ContactForm.route,
            arguments = listOf(
                navArgument("contactId") {
                    type = NavType.IntType
                    defaultValue = -1
                }
            )
        ) { backStackEntry ->
            /**
             * Extrae el argumento contactId del backStackEntry.
             * Si no viene, usa -1 como valor por defecto.
             */
            val contactId = backStackEntry.arguments?.getInt("contactId") ?: -1
            ContactFormScreen(
                navController = navController,
                viewModel = contactViewModel,
                contactId = contactId
            )
        }

        // ─── Productos ─────────────────────────────────────
        composable(route = AppScreens.ProductList.route) {
            ProductListScreen(
                navController = navController,
                viewModel = productViewModel
            )
        }

        composable(
            route = AppScreens.ProductForm.route,
            arguments = listOf(
                navArgument("productId") {
                    type = NavType.IntType
                    defaultValue = -1
                }
            )
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getInt("productId") ?: -1
            ProductFormScreen(
                navController = navController,
                viewModel = productViewModel,
                productId = productId
            )
        }

        // ─── Proveedores ───────────────────────────────────
        composable(route = AppScreens.ProviderList.route) {
            ProviderListScreen(
                navController = navController,
                viewModel = providerViewModel
            )
        }

        composable(
            route = AppScreens.ProviderForm.route,
            arguments = listOf(
                navArgument("providerId") {
                    type = NavType.IntType
                    defaultValue = -1
                }
            )
        ) { backStackEntry ->
            val providerId = backStackEntry.arguments?.getInt("providerId") ?: -1
            ProviderFormScreen(
                navController = navController,
                viewModel = providerViewModel,
                providerId = providerId
            )
        }
    }
}
