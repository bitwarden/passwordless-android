package dev.passwordless.android.rest.serializers

import androidx.credentials.CreatePublicKeyCredentialRequest
import androidx.credentials.GetPublicKeyCredentialOption
import com.google.gson.Gson

class JsonSerializerImpl : JsonSerializer {
    private val gson: Gson

    constructor() {
        val gsonBuilder = Gson().newBuilder()
        gsonBuilder.registerTypeAdapter(ByteArray::class.java, Base64UrlDeserializer())
        gsonBuilder.registerTypeAdapter(CreatePublicKeyCredentialRequest::class.java, CreatePublicKeyCredentialRequestDeserializer())
        gsonBuilder.registerTypeAdapter(GetPublicKeyCredentialOption::class.java, GetPublicKeyCredentialOptionDeserializer())
        gson = gsonBuilder.create()
    }

    override fun get(): Gson {
        return gson
    }


}