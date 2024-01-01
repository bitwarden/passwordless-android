package dev.passwordless.android.rest.serializers

import com.google.gson.Gson

class JsonSerializerImpl : JsonSerializer {
    private val gson: Gson

    constructor() {
        val gsonBuilder = Gson().newBuilder()
        gsonBuilder.registerTypeAdapter(ByteArray::class.java, Base64UrlDeserializer())
        gson = gsonBuilder.create()
    }

    override fun get(): Gson {
        return gson
    }


}