package dev.passwordless.android.rest

import com.google.gson.Gson
import dev.passwordless.android.rest.interceptors.ApikeyHeaderInterceptor
import dev.passwordless.android.rest.interceptors.ClientVersionHeaderInterceptor
import dev.passwordless.android.rest.interceptors.ProblemDetailsInterceptor
import dev.passwordless.android.rest.serializers.JsonSerializerImpl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Factory for creating an instance of the [PasswordlessHttpClient].
 */
object PasswordlessHttpClientFactory {
    /**
     * Creates an instance of the [PasswordlessHttpClient].
     *
     * @param options The options for the client.
     * @return The created client.
     */
    fun create(options: PasswordlessOptions): PasswordlessHttpClient {
        val gson: Gson = JsonSerializerImpl.get()

        val client = OkHttpClient.Builder()
            .addInterceptor(ApikeyHeaderInterceptor(options.apiKey))
            .addInterceptor(ClientVersionHeaderInterceptor())
            .addInterceptor(ProblemDetailsInterceptor())
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()

        return Retrofit.Builder()
            .baseUrl(options.apiUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(PasswordlessHttpClient::class.java)
    }
}
