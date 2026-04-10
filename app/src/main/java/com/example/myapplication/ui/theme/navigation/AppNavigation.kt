package com.example.myapplication.ui.navigation

/**
 * Define todas las rutas de navegación de la aplicación.
 * Cada objeto representa una pantalla con su ruta única.
 * Usar un sealed class garantiza que todas las rutas
 * estén centralizadas y sean seguras en tiempo de compilación.
 */
sealed class AppScreens(val route: String) {

    // ─── Módulo Splash ─────────────────────────────────────
    /**
     * Pantalla de bienvenida que se muestra al abrir la app.
     */
    object Splash : AppScreens("splash")

    // ─── Módulo Home ───────────────────────────────────────
    /**
     * Pantalla de inicio con acceso a todos los módulos.
     */
    object Home : AppScreens("home")

    // ─── Módulo Créditos ───────────────────────────────────
    /**
     * Pantalla de créditos con información de los desarrolladores
     * y enlace al manual de la app.
     */
    object About : AppScreens("about")

    // ─── Módulo Contactos ──────────────────────────────────
    /**
     * Lista de todos los contactos.
     */
    object ContactList : AppScreens("contact_list")

    /**
     * Formulario para agregar o editar un contacto.
     *   - -1  → modo agregar (nuevo contacto)
     *   - >0  → modo editar (contacto existente)
     */
    object ContactForm : AppScreens("contact_form/{contactId}") {
        fun createRoute(contactId: Int = -1) = "contact_form/$contactId"
    }

    // ─── Módulo Productos ──────────────────────────────────
    /**
     * Lista de todos los productos.
     */
    object ProductList : AppScreens("product_list")

    /**
     * Formulario para agregar o editar un producto.
     *   - -1  → modo agregar (nuevo producto)
     *   - >0  → modo editar (producto existente)
     */
    object ProductForm : AppScreens("product_form/{productId}") {
        fun createRoute(productId: Int = -1) = "product_form/$productId"
    }

    // ─── Módulo Proveedores ────────────────────────────────
    /**
     * Lista de todos los proveedores.
     */
    object ProviderList : AppScreens("provider_list")

    /**
     * Formulario para agregar o editar un proveedor.
     *   - -1  → modo agregar (nuevo proveedor)
     *   - >0  → modo editar (proveedor existente)
     */
    object ProviderForm : AppScreens("provider_form/{providerId}") {
        fun createRoute(providerId: Int = -1) = "provider_form/$providerId"
    }

    // ─── Módulo Ventas ─────────────────────────────────────
    /**
     * Lista de todas las ventas realizadas.
     */
    object SaleList : AppScreens("sale_list")

    /**
     * Formulario para crear una nueva venta.
     *   - -1  → nueva venta
     *   - >0  → editar venta existente
     */
    object SaleForm : AppScreens("sale_form/{saleId}") {
        fun createRoute(saleId: Int = -1) = "sale_form/$saleId"
    }

    // ─── Módulo Compras ────────────────────────────────────
    /**
     * Lista de todas las compras realizadas a proveedores.
     */
    object PurchaseList : AppScreens("purchase_list")

    /**
     * Formulario para crear una nueva compra.
     *   - -1  → nueva compra
     *   - >0  → editar compra existente
     */
    object PurchaseForm : AppScreens("purchase_form/{purchaseId}") {
        fun createRoute(purchaseId: Int = -1) = "purchase_form/$purchaseId"
    }
}