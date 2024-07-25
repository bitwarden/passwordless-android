package dev.passwordless.android.rest.serializers

import androidx.credentials.CreatePublicKeyCredentialRequest
import androidx.credentials.CreatePublicKeyCredentialResponse
import androidx.credentials.GetPublicKeyCredentialOption
import androidx.credentials.PublicKeyCredential
import com.google.gson.Gson

/**
 * Implementation for JSON serializers.
 */
object JsonSerializerImpl : JsonSerializer<Any?> {
    private val gson: Gson

    init {
        val gsonBuilder = Gson().newBuilder()

        gsonBuilder.registerTypeAdapter(
            ByteArray::class.java,
            Base64UrlDeserializer(),)

        gsonBuilder.registerTypeAdapter(
            CreatePublicKeyCredentialRequest::class.java,
            CreatePublicKeyCredentialRequestDeserializer(),)

        gsonBuilder.registerTypeAdapter(
            GetPublicKeyCredentialOption::class.java,
            GetPublicKeyCredentialOptionDeserializer(),)

        gsonBuilder.registerTypeAdapter(
            CreatePublicKeyCredentialResponse::class.java,
            CreatePublicKeyCredentialResponseSerializer(),)

        gsonBuilder.registerTypeAdapter(
            PublicKeyCredential::class.java,
            PublicKeyCredentialSerializer(),)

        gson = gsonBuilder.create()
    }

    override fun get(): Gson {
        return gson
    }
}
