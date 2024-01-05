package dev.passwordless.android.rest.interceptors


import okhttp3.Interceptor
import okhttp3.Response

class ApikeyHeaderInterceptor(private val _apiKey: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response = chain.run {
        proceed(
            request()
                .newBuilder()
                .header("Apikey",_apiKey)
                .build()
        )
    }
}
