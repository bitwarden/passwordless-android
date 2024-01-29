package dev.passwordless.sampleapp

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dev.passwordless.android.rest.serializers.Base64UrlDeserializer
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object YourBackendHttpClientFactory {
    fun create(apiURL:String): YourBackendHttpClient {
        val gson: Gson = GsonBuilder()
            .registerTypeAdapter(ByteArray::class.java, Base64UrlDeserializer())
            .create()
        val okHttpLogger= HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

        val client = OkHttpClient.Builder()
//            .addInterceptor(ProblemDetailsInterceptor())
            .addInterceptor(okHttpLogger)
            .build()

        return Retrofit.Builder()
            .baseUrl(apiURL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(YourBackendHttpClient::class.java)
    }
}
