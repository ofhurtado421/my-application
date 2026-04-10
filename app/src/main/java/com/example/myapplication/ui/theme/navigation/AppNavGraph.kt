package com.example.myapplication.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.myapplication.data.entity.AppDatabase
import com.example.myapplication.data.repository.ContactRepository
import com.example.myapplication.data.repository.ProductRepository
import com.example.myapplication.data.repository.ProviderRepository
import com.example.myapplication.data.repository.PurchaseRepository
import com.example.myapplication.data.repository.SaleRepository
import com.example.myapplication.data.viewmodel.ContactViewModel
import com.example.myapplication.data.viewmodel.ProductViewModel
import com.example.myapplication.data.viewmodel.ProviderViewModel
import com.example.myapplication.data.viewmodel.PurchaseViewModel
import com.example.myapplication.data.viewmodel.SaleViewModel
import com.example.myapplication.data.viewmodel.factory.ContactViewModelFactory
import com.example.myapplication.data.viewmodel.factory.ProductViewModelFactory
import com.example.myapplication.data.viewmodel.factory.ProviderViewModelFactory
import com.example.myapplication.data.viewmodel.factory.PurchaseViewModelFactory
import com.example.myapplication.data.viewmodel.factory.SaleViewModelFactory
import com.example.myapplication.ui.screens.contact.ContactFormScreen
import com.example.myapplication.ui.screens.contact.ContactListScreen
import com.example.myapplication.ui.screens.home.HomeScreen
import com.example.myapplication.ui.screens.product.ProductFormScreen
import com.example.myapplication.ui.screens.product.ProductListScreen
import com.example.myapplication.ui.screens.provider.ProviderFormScreen
import com.example.myapplication.ui.screens.provider.ProviderListScreen
import com.example.myapplication.ui.screens.purchase.PurchaseFormScreen
import com.example.myapplication.ui.screens.purchase.PurchaseListScreen
import com.example.myapplication.ui.screens.sale.SaleFormScreen
import com.example.myapplication.ui.screens.sale.SaleListScreen
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

    val context = LocalContext.current
    val database = AppDatabase.getInstance(context)

    // ─── Repositorios ──────────────────────────────────────
    val contactRepository = ContactRepository(database.contactDao())
    val productRepository = ProductRepository(database.productDao())
    val providerRepository = ProviderRepository(database.providerDao())
    /**
     * ✅ Nuevos repositorios de ventas y compras
     */
    val saleRepository = SaleRepository(database.saleDao())
    val purchaseRepository = PurchaseRepository(database.purchaseDao())

    // ─── ViewModels ────────────────────────────────────────
    val contactViewModel: ContactViewModel = viewModel(
        factory = ContactViewModelFactory(contactRepository)
    )
    val productViewModel: ProductViewModel = viewModel(
        factory = ProductViewModelFactory(productRepository)
    )
    val providerViewModel: ProviderViewModel = viewModel(
        factory = ProviderViewModelFactory(providerRepository)
    )
    /**
     * ✅ Nuevos ViewModels de ventas y compras
     */
    val saleViewModel: SaleViewModel = viewModel(
        factory = SaleViewModelFactory(saleRepository)
    )
    val purchaseViewModel: PurchaseViewModel = viewModel(
        factory = PurchaseViewModelFactory(purchaseRepository)
    )

    // ─── NavHost ───────────────────────────────────────────
    NavHost(
        navController = navController,
        startDestination = AppScreens.Splash.route
    ) {

        // ─── Splash ────────────────────────────────────────
        composable(route = AppScreens.Splash.route) {
            SplashScreen(navController = navController)
        }

        // ─── Home ──────────────────────────────────────────
        composable(route = AppScreens.Home.route) {
            HomeScreen(navController = navController)
        }

        // ─── Créditos ──────────────────────────────────────
        composable(route = AppScreens.About.route) {
            AboutScreen(navController = navController)
        }

        // ─── Contactos ─────────────────────────────────────
        composable(route = AppScreens.ContactList.route) {
            ContactListScreen(
                navController = navController,
                viewModel = contactViewModel
            )
        }
        composable(
            route = AppScreens.ContactForm.route,
            arguments = listOf(
                navArgument("contactId") {
                    type = NavType.IntType
                    defaultValue = -1
                }
            )
        ) { backStackEntry ->
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

        // ─── Ventas ────────────────────────────────────────
        /**
         * Lista de todas las ventas.
         */
        composable(route = AppScreens.SaleList.route) {
            SaleListScreen(
                navController = navController,
                viewModel = saleViewModel
            )
        }
        /**
         * Formulario para crear una nueva venta.
         * Recibe saleId como argumento:
         *   -1  → nueva venta
         *   >0  → editar venta existente
         */
        composable(
            route = AppScreens.SaleForm.route,
            arguments = listOf(
                navArgument("saleId") {
                    type = NavType.IntType
                    defaultValue = -1
                }
            )
        ) {
            SaleFormScreen(
                navController = navController,
                saleViewModel = saleViewModel,
                productViewModel = productViewModel
            )
        }

        // ─── Compras ───────────────────────────────────────
        /**
         * Lista de todas las compras.
         */
        composable(route = AppScreens.PurchaseList.route) {
            PurchaseListScreen(
                navController = navController,
                viewModel = purchaseViewModel
            )
        }
        /**
         * Formulario para crear una nueva compra.
         * Recibe purchaseId como argumento:
         *   -1  → nueva compra
         *   >0  → editar compra existente
         */
        composable(
            route = AppScreens.PurchaseForm.route,
            arguments = listOf(
                navArgument("purchaseId") {
                    type = NavType.IntType
                    defaultValue = -1
                }
            )
        ) {
            PurchaseFormScreen(
                navController = navController,
                purchaseViewModel = purchaseViewModel,
                productViewModel = productViewModel
            )
        }
    }
}