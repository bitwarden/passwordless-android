package dev.passwordless.android.rest.interceptors

import dev.passwordless.android.rest.exceptions.PasswordlessApiException
import dev.passwordless.android.rest.exceptions.ProblemDetails
import dev.passwordless.android.rest.serializers.JsonSerializer
import dev.passwordless.android.rest.serializers.JsonSerializerImpl
import okhttp3.Interceptor
import okhttp3.Response

class ApikeyHeaderInterceptor(val _apiKey: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response = chain.run {
        proceed(
            request()
                .newBuilder()
                .header("Apikey",_apiKey)
                .build()
        )
    }
}
