package cz.vratislavjindra.alzacasestudy.core.util

sealed class Resource<T>(val data: T? = null) {

    class Loading<T>(data: T? = null) : Resource<T>(data = data)

    class Success<T>(data: T) : Resource<T>(data = data)

    class Error<T>(val error: AlzaError, val errorMessage: String, data: T? = null) :
        Resource<T>(data = data)
}