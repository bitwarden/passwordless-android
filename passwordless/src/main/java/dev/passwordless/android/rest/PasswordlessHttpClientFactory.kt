package dev.passwordless.android.rest

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dev.passwordless.android.rest.interceptors.ApikeyHeaderInterceptor
import dev.passwordless.android.rest.interceptors.ProblemDetailsInterceptor
import dev.passwordless.android.rest.serializers.Base64UrlDeserializer
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object PasswordlessHttpClientFactory {
    fun create(options: PasswordlessOptions): PasswordlessHttpClient {
        val gson: Gson = GsonBuilder()
            .registerTypeAdapter(ByteArray::class.java, Base64UrlDeserializer())
            .create()

        val client = OkHttpClient.Builder()
            .addInterceptor(ApikeyHeaderInterceptor(options.apiKey))
            .addInterceptor(ProblemDetailsInterceptor())
            .build()

        return Retrofit.Builder()
            .baseUrl(options.apiUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(PasswordlessHttpClient::class.java)
    }
}
