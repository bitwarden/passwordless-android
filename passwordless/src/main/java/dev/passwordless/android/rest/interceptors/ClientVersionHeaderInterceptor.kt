package dev.passwordless.android.rest.interceptors

import dev.passwordless.android.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Interceptor for adding the SDK version to the request headers.
 */
class ClientVersionHeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response = chain.run {
        val versionName = BuildConfig.VERSION_NAME
        proceed(
            request()
                .newBuilder()
                .header("Client-Version", "android-$versionName")
                .build(),
        )
    }
}
