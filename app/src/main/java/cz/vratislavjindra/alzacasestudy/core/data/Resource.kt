package cz.vratislavjindra.alzacasestudy.core.data

import cz.vratislavjindra.alzacasestudy.core.utils.ErrorMessage

/**
 * A generic class that holds a value or an exception.
 */
sealed class Resource<T> {

    data class Loading<T>(val data: T? = null) : Resource<T>()

    data class Success<T>(val data: T) : Resource<T>()

    class Error<T>(val errorMessage: ErrorMessage, val data: T? = null) : Resource<T>()
}