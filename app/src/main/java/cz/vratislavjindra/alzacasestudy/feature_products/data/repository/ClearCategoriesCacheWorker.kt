package cz.vratislavjindra.alzacasestudy.feature_products.data.repository

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import cz.vratislavjindra.alzacasestudy.feature_products.data.local.ProductDatabase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

const val CLEAR_CATEGORIES_CACHE_WORKER_TAG = "clear_categories_cache_worker"

@HiltWorker
class ClearCategoriesCacheWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val db: ProductDatabase
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        db.categoryDao.deleteAllCategories()
        return Result.success()
    }
}