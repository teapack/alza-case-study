package cz.vratislavjindra.alzacasestudy.feature_products.domain.use_case

import android.content.Context
import cz.vratislavjindra.alzacasestudy.core.util.AlzaError
import cz.vratislavjindra.alzacasestudy.core.util.Resource
import cz.vratislavjindra.alzacasestudy.feature_products.domain.model.Product
import cz.vratislavjindra.alzacasestudy.feature_products.domain.repository.ProductRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetProductDetail @Inject constructor(
    @ApplicationContext private val context: Context,
    private val productRepository: ProductRepository
) {

    suspend operator fun invoke(productId: Int?): Flow<Resource<Product>> {
        return if (productId == null) {
            val error = AlzaError.NO_SELECTED_PRODUCT
            flow {
                emit(
                    value = Resource.Error(
                        error = error,
                        errorMessage = context.getString(error.errorMessageResId)
                    )
                )
            }
        } else {
            productRepository.getProductDetail(id = productId)
        }
    }
}