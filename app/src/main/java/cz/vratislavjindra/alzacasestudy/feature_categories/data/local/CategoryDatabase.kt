package cz.vratislavjindra.alzacasestudy.feature_categories.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import cz.vratislavjindra.alzacasestudy.feature_categories.data.local.entity.CategoryEntity

@Database(
    entities = [CategoryEntity::class],
    version = 1
)
abstract class CategoryDatabase : RoomDatabase() {

    abstract val categoryDao: CategoryDao

    companion object {

        const val DATABASE_NAME = "category_db"
    }
}