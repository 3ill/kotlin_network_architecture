package org.example.core.network

inline fun <T, R> ApiResult<T>.map(transform: (T) -> R): ApiResult<R> {
    return when(this) {
        is ApiResult.Success -> {
            ApiResult.Success(transform(data))
        }

        is ApiResult.Failure -> {
            this
        }
    }
}