package cz.vratislavjindra.alzacasestudy.feature_products.data.repository

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import cz.vratislavjindra.alzacasestudy.feature_products.data.local.ProductDatabase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

const val CLEAR_PRODUCTS_CACHE_WORKER_TAG = "clear_products_cache_worker"
const val CLEAR_PRODUCTS_CACHE_WORKER_TAG_PREFIX = "clear_products_cache_worker_"
const val CLEAR_PRODUCTS_CACHE_INPUT_DATA_KEY_CATEGORY_ID = "category_id"

const val CLEAR_PRODUCT_CACHE_WORKER_TAG = "clear_product_cache_worker"
const val CLEAR_PRODUCT_CACHE_WORKER_TAG_PREFIX = "clear_product_cache_worker_"
const val CLEAR_PRODUCT_CACHE_INPUT_DATA_KEY_PRODUCT_ID = "product_id"

@HiltWorker
class ClearProductsCacheWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val db: ProductDatabase
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return if (tags.contains(element = CLEAR_PRODUCTS_CACHE_WORKER_TAG)) {
            val categoryId = inputData.getInt(
                CLEAR_PRODUCTS_CACHE_INPUT_DATA_KEY_CATEGORY_ID,
                0
            )
            db.productDao.deleteProductsByCategory(categoryId = categoryId)
            Result.success()
        } else if (tags.contains(element = CLEAR_PRODUCT_CACHE_WORKER_TAG)) {
            val productId = inputData.getInt(
                CLEAR_PRODUCT_CACHE_INPUT_DATA_KEY_PRODUCT_ID,
                0
            )
            db.productDao.deleteProductById(id = productId)
            Result.success()
        } else {
            Result.failure()
        }
    }
}

