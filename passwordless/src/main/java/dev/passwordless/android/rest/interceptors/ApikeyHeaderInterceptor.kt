package dev.passwordless.android.rest.interceptors

import okhttp3.Interceptor
import okhttp3.Response

/**
 * Interceptor for adding the API key to the request headers.
 * @param apiKey The API key to be added to the request headers.
 */
class ApikeyHeaderInterceptor(private val apiKey: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response = chain.run {
        proceed(
            request()
                .newBuilder()
                .header("Apikey", apiKey)
                .build(),
        )
    }
}
