package com.example.myapplication.data.entity

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myapplication.data.dao.ContactDao
import com.example.myapplication.data.dao.ProductDao
import com.example.myapplication.data.dao.ProviderDao
import com.example.myapplication.data.dao.PurchaseDao
import com.example.myapplication.data.dao.SaleDao

/**
 * Clase principal de la base de datos Room.
 * Aquí se registran todas las entidades (tablas) y se exponen los DAOs.
 *
 * @Database define:
 *   - entities: lista de todas las tablas de la base de datos
 *   - version: versión de la base de datos, se incrementa cada vez
 *              que se modifica la estructura de alguna tabla
 *   - exportSchema: si es true guarda un historial del esquema en un archivo JSON,
 *                   false para proyectos simples o en desarrollo
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
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    /**
     * Expone el DAO de contactos.
     * Room genera automáticamente la implementación de esta función.
     */
    abstract fun contactDao(): ContactDao

    /**
     * Expone el DAO de productos.
     * Room genera automáticamente la implementación de esta función.
     */
    abstract fun productDao(): ProductDao

    /**
     * Expone el DAO de proveedores.
     * Room genera automáticamente la implementación de esta función.
     */
    abstract fun providerDao(): ProviderDao

    abstract fun saleDao(): SaleDao

    abstract fun purchaseDao(): PurchaseDao




    /**
     * Companion object que implementa el patrón Singleton.
     * Garantiza que solo exista UNA instancia de la base de datos
     * en toda la aplicación, evitando problemas de concurrencia
     * y uso innecesario de memoria.
     */
    companion object {

        /**
         * @Volatile garantiza que el valor de INSTANCE sea siempre
         * el más actualizado y visible para todos los hilos.
         */
        @Volatile
        private var INSTANCE: AppDatabase? = null

        /**
         * Retorna la instancia única de la base de datos.
         * Si no existe, la crea. Si ya existe, la reutiliza.
         * @param context Se usa el applicationContext para evitar
         *                memory leaks con Activities o Fragments.
         * @return La instancia única de AppDatabase.
         */
        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"

                )
                    /**
                     * ⚠️ fallbackToDestructiveMigration borra y recrea
                     * la base de datos cuando sube la versión.
                     * Solo usar en desarrollo, no en producción.
                     */
                    .fallbackToDestructiveMigration()

                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}