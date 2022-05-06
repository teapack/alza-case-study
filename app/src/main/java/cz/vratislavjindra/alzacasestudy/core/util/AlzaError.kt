package cz.vratislavjindra.alzacasestudy.core.util

import androidx.annotation.StringRes
import cz.vratislavjindra.alzacasestudy.R

enum class AlzaError(@StringRes val errorMessageResId: Int) {

    FEATURE_NOT_IMPLEMENTED_YET(errorMessageResId = R.string.error_not_implemented_yet),
    HTTP_ERROR(errorMessageResId = R.string.error_http),
    INVALID_CATEGORY(errorMessageResId = R.string.error_invalid_category),
    INVALID_PRODUCT(errorMessageResId = R.string.error_invalid_product),
    TIMEOUT(errorMessageResId = R.string.error_timeout)
}