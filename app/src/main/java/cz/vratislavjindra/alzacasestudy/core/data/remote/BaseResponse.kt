package cz.vratislavjindra.alzacasestudy.core.data.remote

abstract class BaseResponse<T> {

    abstract val data: T?
    abstract val errorCode: Int
    abstract val errorMessage: String?

    fun isResponseOk(): Boolean {
        return data != null && errorCode == 0 && errorMessage == null
    }

    fun getDataOrThrow(): T {
        return data.let {
            if (it != null && errorCode == 0 && errorMessage == null) {
                it
            } else {
                throw Exception()
            }
        }
    }
}