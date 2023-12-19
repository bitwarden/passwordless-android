package dev.passwordless.android.rest.converters

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject

object PublicKeyCredentialConverter {
    fun convertJson(inputJson: String): JsonObject {
        val gson = Gson()

        val json = gson.fromJson(inputJson, JsonObject::class.java)
        val clientExtensionResults = json.getAsJsonObject("clientExtensionResults")
        json.remove("clientExtensionResults")
        json.add("extensions",clientExtensionResults)

        return json
    }

}