package cz.vratislavjindra.alzacasestudy.feature_products.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import cz.vratislavjindra.alzacasestudy.feature_products.data.local.entity.CategoryEntity
import cz.vratislavjindra.alzacasestudy.feature_products.data.local.entity.ProductEntity

@Database(
    entities = [
        CategoryEntity::class,
        ProductEntity::class
    ],
    version = 1
)
abstract class ProductDatabase : RoomDatabase() {

    abstract val categoryDao: CategoryDao

    abstract val productDao: ProductDao

    companion object {

        const val DATABASE_NAME = "product_db"
    }
}