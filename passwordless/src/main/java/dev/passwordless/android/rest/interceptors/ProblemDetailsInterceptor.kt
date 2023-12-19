package dev.passwordless.android.rest.interceptors

import dev.passwordless.android.rest.exceptions.PasswordlessApiException
import dev.passwordless.android.rest.exceptions.ProblemDetails
import dev.passwordless.android.rest.serializers.JsonSerializer
import dev.passwordless.android.rest.serializers.JsonSerializerImpl
import okhttp3.Interceptor
import okhttp3.Response

class ProblemDetailsInterceptor(val _serializer: JsonSerializer) : Interceptor {
    constructor() : this(JsonSerializerImpl())

    @Throws(PasswordlessApiException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())

//        if (!response.isSuccessful) {
//            val contentType = response.headers().get("Content-Type")
//            if (contentType != "application/problem+json") {
//                return response
//            }
//
//            val json = response.body()!!.string()
//            val problemDetails = _serializer.get().fromJson(json, ProblemDetails::class.java)
//            throw PasswordlessApiException(problemDetails)
//        }

        return response
    }
}
