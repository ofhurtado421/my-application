package com.example.myapplication.data.entity

import com.example.myapplication.data.dao.ContactDao
import com.example.myapplication.data.dao.ProductDao
import com.example.myapplication.data.dao.ProviderDao

/**
 * Clase encargada de precargar datos iniciales en la base de datos.
 * Se ejecuta una sola vez cuando la app se instala por primera vez.
 * Contiene datos de prueba de una droguería colombiana.
 *
 * @param productDao DAO para insertar productos.
 * @param providerDao DAO para insertar proveedores.
 * @param contactDao DAO para insertar clientes.
 */
class DatabaseSeeder(
    private val productDao: ProductDao,
    private val providerDao: ProviderDao,
    private val contactDao: ContactDao
) {

    /**
     * Inserta todos los datos de prueba en la base de datos.
     * Se ejecuta en una corrutina desde AppDatabase.
     */
    suspend fun seed() {
        seedProviders()
        seedProducts()
        seedContacts()
    }

    // ─── Proveedores ───────────────────────────────────────
    /**
     * Inserta 3 proveedores de droguería colombianos.
     */
    private suspend fun seedProviders() {
        val providers = listOf(
            ProviderEntity(
                businessName = "Distribuidora Médica del Valle S.A.S",
                nit = "900123456-1",
                phone = "3001234567",
                address = "Calle 15 #32-10, Cali, Valle del Cauca"
            ),
            ProviderEntity(
                businessName = "Laboratorios Farmacéuticos Andinos",
                nit = "800987654-2",
                phone = "3109876543",
                address = "Carrera 7 #45-20, Bogotá D.C"
            ),
            ProviderEntity(
                businessName = "Dromedical Colombia Ltda",
                nit = "700456789-3",
                phone = "3207654321",
                address = "Avenida 30 #12-50, Medellín, Antioquia"
            )
        )
        providers.forEach { providerDao.insert(it) }
    }

    // ─── Productos ─────────────────────────────────────────
    /**
     * Inserta 5 productos típicos de una droguería colombiana.
     * Cada producto tiene precio de compra y precio de venta.
     */
    private suspend fun seedProducts() {
        val products = listOf(
            ProductEntity(
                barcode = "7702001010101",
                name = "Acetaminofén 500mg x10",
                description = "Analgésico y antipirético. Caja por 10 tabletas de 500mg.",
                category = "Analgésicos",
                unitType = "Caja",
                manufacturer = "Tecnoquímicas",
                brand = "Dolex",
                salePrice = 3500.0,
                purchasePrice = 2000.0,
                isActive = true
            ),
            ProductEntity(
                barcode = "7702001020202",
                name = "Ibuprofeno 400mg x10",
                description = "Antiinflamatorio no esteroideo. Caja por 10 tabletas de 400mg.",
                category = "Antiinflamatorios",
                unitType = "Caja",
                manufacturer = "Abbott",
                brand = "Advil",
                salePrice = 5200.0,
                purchasePrice = 3500.0,
                isActive = true
            ),
            ProductEntity(
                barcode = "7702001030303",
                name = "Loratadina 10mg x10",
                description = "Antihistamínico para alergias. Caja por 10 tabletas de 10mg.",
                category = "Antihistamínicos",
                unitType = "Caja",
                manufacturer = "Bayer",
                brand = "Clarityne",
                salePrice = 8900.0,
                purchasePrice = 6000.0,
                isActive = true
            ),
            ProductEntity(
                barcode = "7702001040404",
                name = "Omeprazol 20mg x10",
                description = "Protector gástrico. Caja por 10 cápsulas de 20mg.",
                category = "Gastrointestinales",
                unitType = "Caja",
                manufacturer = "Tecnoquímicas",
                brand = "Omeprazol TQ",
                salePrice = 6500.0,
                purchasePrice = 4000.0,
                isActive = true
            ),
            ProductEntity(
                barcode = "7702001050505",
                name = "Suero Oral 500ml",
                description = "Sales de rehidratación oral. Frasco por 500ml.",
                category = "Hidratación",
                unitType = "Frasco",
                manufacturer = "Genfar",
                brand = "Suero Oral Genfar",
                salePrice = 4800.0,
                purchasePrice = 3000.0,
                isActive = true
            )
        )
        products.forEach { productDao.insert(it) }
    }

    // ─── Clientes ──────────────────────────────────────────
    /**
     * Inserta 5 clientes de prueba.
     */
    private suspend fun seedContacts() {
        val contacts = listOf(
            ContactEntity(
                name = "María Fernanda López",
                phone = "3001112233"
            ),
            ContactEntity(
                name = "Carlos Andrés Martínez",
                phone = "3104445566"
            ),
            ContactEntity(
                name = "Ana Lucía Gómez",
                phone = "3207778899"
            ),
            ContactEntity(
                name = "Jorge Iván Ramírez",
                phone = "3012223344"
            ),
            ContactEntity(
                name = "Laura Melissa Torres",
                phone = "3155556677"
            )
        )
        contacts.forEach { contactDao.insert(it) }
    }
}