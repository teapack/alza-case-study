package cz.vratislavjindra.alzacasestudy.feature_products.domain.use_case

import android.content.Context
import cz.vratislavjindra.alzacasestudy.core.util.AlzaError
import cz.vratislavjindra.alzacasestudy.core.util.Resource
import cz.vratislavjindra.alzacasestudy.feature_products.domain.model.ProductListItem
import cz.vratislavjindra.alzacasestudy.feature_products.domain.repository.ProductRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetProducts @Inject constructor(
    @ApplicationContext private val context: Context,
    private val productRepository: ProductRepository
) {

    suspend operator fun invoke(categoryId: Int?): Flow<Resource<List<ProductListItem>>> {
        return if (categoryId == null) {
            val error = AlzaError.NO_SELECTED_CATEGORY
            flow {
                emit(
                    value = Resource.Error(
                        error = error,
                        errorMessage = context.getString(error.errorMessageResId)
                    )
                )
            }
        } else {
            productRepository.getProductsByCategory(categoryId = categoryId)
        }
    }
}