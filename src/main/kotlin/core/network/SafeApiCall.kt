package org.example.core.network

import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import kotlinx.coroutines.CancellationException
import kotlinx.io.IOException
import kotlinx.serialization.SerializationException

suspend inline fun <reified T> safeApiCall(
    crossinline request: suspend () -> HttpResponse
): ApiResult<T> {
    return try {
        val response = request()

        if (!response.status.isSuccess()) {
            ApiResult.Failure(
                ApiError.Http(
                    statusCode = response.status.value,
                    responseBody = response.bodyAsText()
                )
            )
        } else {
            ApiResult.Success(
                data = response.body<T>()
            )
        }
    } catch (exc: CancellationException) {
        throw exc
    } catch (exc: IOException) {
        ApiResult.Failure(
            ApiError.Network(exc.message)
        )
    } catch (exc: SerializationException) {
        ApiResult.Failure(
            ApiError.Serialization(exc.message)
        )
    } catch (exc: Exception) {
        ApiResult.Failure(ApiError.Unknown(exc.message))
    }
}

