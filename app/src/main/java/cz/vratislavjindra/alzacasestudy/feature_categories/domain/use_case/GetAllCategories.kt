package cz.vratislavjindra.alzacasestudy.feature_categories.domain.use_case

import cz.vratislavjindra.alzacasestudy.core.data.Resource
import cz.vratislavjindra.alzacasestudy.feature_categories.domain.model.Category
import cz.vratislavjindra.alzacasestudy.feature_categories.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllCategories @Inject constructor(
    private val categoryRepository: CategoryRepository
) {

    suspend operator fun invoke(): Flow<Resource<List<Category>>> {
        return categoryRepository.getAllCategories()
    }
}