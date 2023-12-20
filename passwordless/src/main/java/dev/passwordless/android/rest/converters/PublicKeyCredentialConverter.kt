package dev.passwordless.android.rest.converters

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject


object PublicKeyCredentialConverter {
    /**
     * This converted is used because of naming mismatch of clientExtensionResults and extensions int the response JSON.
     * @param inputJson JSON from the PublicKeyCredential using .toJson() function.
     * @return Updated json with renamed value
     */
    fun convertJson(inputJson: String): JsonObject {
        val gson = Gson()

        val json = gson.fromJson(inputJson, JsonObject::class.java)
        val clientExtensionResults = json.getAsJsonObject("clientExtensionResults")
        json.remove("clientExtensionResults")
        json.add("extensions",clientExtensionResults)

        return json
    }

}