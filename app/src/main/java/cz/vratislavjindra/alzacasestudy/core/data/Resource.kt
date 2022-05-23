package cz.vratislavjindra.alzacasestudy.core.data

import cz.vratislavjindra.alzacasestudy.core.utils.ErrorMessage

sealed class Resource<T> {

    data class Loading<T>(val data: T? = null) : Resource<T>()

    data class Success<T>(val data: T) : Resource<T>()

    data class Error<T>(val errorMessage: ErrorMessage, val data: T? = null) : Resource<T>()
}