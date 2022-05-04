package cz.vratislavjindra.alzacasestudy.feature_categories.domain.repository

import cz.vratislavjindra.alzacasestudy.core.util.Resource
import cz.vratislavjindra.alzacasestudy.feature_categories.domain.model.Category
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {

    suspend fun getAllCategories(): Flow<Resource<List<Category>>>
}