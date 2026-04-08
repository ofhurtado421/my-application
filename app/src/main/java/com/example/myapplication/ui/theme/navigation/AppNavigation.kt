package com.example.myapplication.ui.navigation

/**
 * Define todas las rutas de navegación de la aplicación.
 * Cada objeto representa una pantalla con su ruta única.
 * Usar un sealed class garantiza que todas las rutas
 * estén centralizadas y sean seguras en tiempo de compilación.
 */
sealed class AppScreens(val route: String) {


    // ─── Módulo Splash ─────────────────────────────────────

    /** Pantalla de bienvenida (Splash Screen) */
    object Splash : AppScreens("splash")

    // ─── Módulo Home ───────────────────────────────────────
    /** Pantalla de inicio con acceso a los 3 módulos */
    object Home : AppScreens("home")

    // ─── Módulo Contactos ──────────────────────────────────
    /** Lista de todos los contactos */
    object ContactList : AppScreens("contact_list")

    /**
     * Formulario para agregar o editar un contacto.
     * El parámetro {contactId} es opcional:
     *   - -1  → modo agregar (nuevo contacto)
     *   - >0  → modo editar (contacto existente)
     */
    object ContactForm : AppScreens("contact_form/{contactId}") {
        /** Genera la ruta con el ID del contacto a editar */
        fun createRoute(contactId: Int = -1) = "contact_form/$contactId"
    }

    // ─── Módulo Productos ──────────────────────────────────
    /** Lista de todos los productos */
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
    /** Lista de todos los proveedores */
    object ProviderList : AppScreens("provider_list")

    /**
     * Formulario para agregar o editar un proveedor.
     *   - -1  → modo agregar (nuevo proveedor)
     *   - >0  → modo editar (proveedor existente)
     */
    object ProviderForm : AppScreens("provider_form/{providerId}") {
        fun createRoute(providerId: Int = -1) = "provider_form/$providerId"
    }
}