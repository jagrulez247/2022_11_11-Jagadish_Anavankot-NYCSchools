package com.education.nycschools.domain.network

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import com.education.nycschools.domain.BuildConfig

class NycSchoolApiInterceptor : Interceptor {

    companion object {
        private const val ACCEPT_HEADER = "Accept"
        private const val APP_TOKEN = "X-App-Token"
        private const val AUTHORIZATION = "Authorization"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain
            .request()
            .newBuilder()
            .addRequestHeaders()
            .build()
        val response = chain.proceed(request)

        val requestUrl = request.url.toString()
        val responseBody = response.body?.toString() ?: ""
        val responseCode = response.code.toString()

        Log.d(
            "NycSchoolApiInterceptor",
            "Response for $requestUrl - \n$responseCode: $responseBody"
        )

        return response
    }

    private fun Request.Builder.addRequestHeaders(): Request.Builder {
        removeHeader(ACCEPT_HEADER)
        addHeader(ACCEPT_HEADER, "*/*")
        addHeader(APP_TOKEN, BuildConfig.APP_TOKEN)
        //addHeader(AUTHORIZATION, "Basic ${BuildConfig.API_KEY}")
        return this
    }
}