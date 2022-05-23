package cz.vratislavjindra.alzacasestudy.feature_categories.data

import cz.vratislavjindra.alzacasestudy.feature_categories.domain.model.Category

interface CategoryDataSource {

    suspend fun deleteAllCategories()

    suspend fun getAllCategories(): Result<List<Category>>

    suspend fun saveCategories(categories: List<Category>)
}