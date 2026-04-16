
package com.example.myapplication.data.entity

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.myapplication.data.dao.ContactDao
import com.example.myapplication.data.dao.ProductDao
import com.example.myapplication.data.dao.ProviderDao
import com.example.myapplication.data.dao.PurchaseDao
import com.example.myapplication.data.dao.SaleDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Clase principal de la base de datos Room.
 * Aquí se registran todas las entidades (tablas) y se exponen los DAOs.
 *
 * @Database define:
 *   - entities: lista de todas las tablas de la base de datos
 *   - version: versión de la base de datos, se incrementa cada vez
 *              que se modifica la estructura de alguna tabla
 *   - exportSchema: false para proyectos simples o en desarrollo
 */
@Database(
    entities = [
        ContactEntity::class,
        ProductEntity::class,
        ProviderEntity::class,
        SaleEntity::class,
        SaleDetailEntity::class,
        PurchaseEntity::class,
        PurchaseDetailEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    /**
     * Expone el DAO de contactos.
     */
    abstract fun contactDao(): ContactDao

    /**
     * Expone el DAO de productos.
     */
    abstract fun productDao(): ProductDao

    /**
     * Expone el DAO de proveedores.
     */
    abstract fun providerDao(): ProviderDao

    /**
     * Expone el DAO de ventas.
     */
    abstract fun saleDao(): SaleDao

    /**
     * Expone el DAO de compras.
     */
    abstract fun purchaseDao(): PurchaseDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .fallbackToDestructiveMigration()
                    /**
                     * addCallback ejecuta código cuando la base
                     * de datos se crea por primera vez.
                     * onCreate solo se llama UNA vez al instalar la app.
                     */
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            /**
                             * Lanza una corrutina en segundo plano
                             * para insertar los datos sin bloquear
                             * el hilo principal.
                             */
                            CoroutineScope(Dispatchers.IO).launch {
                                val database = getInstance(context)
                                DatabaseSeeder(
                                    productDao = database.productDao(),
                                    providerDao = database.providerDao(),
                                    contactDao = database.contactDao()
                                ).seed()
                            }
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}