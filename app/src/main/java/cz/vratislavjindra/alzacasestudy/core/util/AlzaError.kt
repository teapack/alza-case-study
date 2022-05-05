package cz.vratislavjindra.alzacasestudy.core.util

import androidx.annotation.StringRes
import cz.vratislavjindra.alzacasestudy.R

enum class AlzaError(@StringRes val errorMessageResId: Int) {

    HTTP_ERROR(errorMessageResId = R.string.error_http),
    INVALID_CATEGORY(errorMessageResId = R.string.error_invalid_category),
    TIMEOUT(errorMessageResId = R.string.error_timeout)
}