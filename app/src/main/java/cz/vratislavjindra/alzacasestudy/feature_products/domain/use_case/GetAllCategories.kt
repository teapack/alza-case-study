package cz.vratislavjindra.alzacasestudy.feature_products.domain.use_case

import cz.vratislavjindra.alzacasestudy.core.util.Resource
import cz.vratislavjindra.alzacasestudy.feature_products.domain.model.Category
import cz.vratislavjindra.alzacasestudy.feature_products.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllCategories @Inject constructor(
    private val categoryRepository: CategoryRepository
) {

    suspend operator fun invoke(): Flow<Resource<List<Category>>> {
        return categoryRepository.getAllCategories()
    }
}