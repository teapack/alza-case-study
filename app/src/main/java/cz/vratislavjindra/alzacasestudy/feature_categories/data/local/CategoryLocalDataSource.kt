package cz.vratislavjindra.alzacasestudy.feature_categories.data.local

import cz.vratislavjindra.alzacasestudy.feature_categories.data.CategoryDataSource
import cz.vratislavjindra.alzacasestudy.feature_categories.data.local.entity.CategoryEntity
import cz.vratislavjindra.alzacasestudy.feature_categories.domain.model.Category
import javax.inject.Inject

class CategoryLocalDataSource @Inject constructor(
    private val dao: CategoryDao
): CategoryDataSource {

    override suspend fun deleteAllCategories() {
        dao.deleteAllCategories()
    }

    override suspend fun getAllCategories(): Result<List<Category>> {
        return Result.success(value = dao.getAllCategories().map { it.toCategory() })
    }

    override suspend fun saveCategories(categories: List<Category>) {
        dao.insertCategories(categories = categories.map { CategoryEntity(category = it) })
    }
}