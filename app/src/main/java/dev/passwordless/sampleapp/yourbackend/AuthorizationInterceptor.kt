package dev.passwordless.sampleapp.yourbackend

import okhttp3.Interceptor
import okhttp3.Response

class AuthorizationInterceptor(private val token: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val requestBuilder = originalRequest.newBuilder()
            .addHeader("Authorization", "Bearer $token")

        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}