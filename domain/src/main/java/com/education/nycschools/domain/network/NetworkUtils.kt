package com.education.nycschools.domain.network

import com.education.nycschools.domain.models.ApiError
import retrofit2.Response
import com.education.nycschools.domain.models.DataFetchResult
import retrofit2.Retrofit
import java.io.IOException

internal object NetworkUtils {
    suspend fun <T> Retrofit.getResponse(
        request: suspend () -> Response<T>,
        defaultErrorMessage: String
    ): DataFetchResult<T> {
        return try {
            println("I'm working in thread ${Thread.currentThread().name}")
            val result = request.invoke()
            if (result.isSuccessful) {
                return DataFetchResult.success(result.body())
            } else {
                val errorResponse = parseError(result, this)
                DataFetchResult.error(errorResponse?.status_message ?: defaultErrorMessage, errorResponse)
            }
        } catch (e: Throwable) {
            DataFetchResult.error("Unknown Error", null)
        }
    }

    private fun parseError(response: Response<*>, retrofit: Retrofit): ApiError? {
        return try {
            retrofit
                .responseBodyConverter<ApiError>(ApiError::class.java, arrayOfNulls(0))
                .convert(response.errorBody()!!)
        } catch (e: IOException) {
            ApiError()
        }
    }
}