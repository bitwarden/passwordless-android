package dev.passwordless.android.rest.interceptors

import dev.passwordless.android.rest.exceptions.PasswordlessApiException
import dev.passwordless.android.rest.exceptions.ProblemDetails
import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ProblemDetailsInterceptor : Interceptor {
    @Throws(PasswordlessApiException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())

        if (!response.isSuccessful) {
            val gson = GsonConverterFactory.create().responseBodyConverter(
                ProblemDetails::class.java, emptyArray(), Retrofit.Builder().build()
            )

            val problemDetails = try {
                response.body()?.let {
                    gson!!.convert(it) as ProblemDetails?
                }
            } catch (e: Exception) {
                null
            }

            if (problemDetails != null) {
                throw PasswordlessApiException(problemDetails)
            }
        }

        return response
    }
}
