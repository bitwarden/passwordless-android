package dev.passwordless.android.rest

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dev.passwordless.android.rest.serializers.Base64UrlDeserializer
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object PasswordlessHttpClientFactory {
    fun create(options: PasswordlessOptions): PasswordlessHttpClient {
        val gson: Gson = GsonBuilder()
            .registerTypeAdapter(ByteArray::class.java, Base64UrlDeserializer())
            .create()

        return Retrofit.Builder()
            .baseUrl(options.apiUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(PasswordlessHttpClient::class.java)
    }
}