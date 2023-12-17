package dev.passwordless.android.rest.converters

import com.google.gson.Gson
import com.google.gson.JsonObject

object PublicKeyCredentialConverter {
    fun convertJson(inputJson: String): String {
        val gson = Gson()

        // Parse the input JSON string
        val json = gson.fromJson(inputJson, JsonObject::class.java)

        // Create the output JSON object
        val outputJson = JsonObject()

        // Extract fields from the input JSON and add them to the output JSON
        outputJson.addProperty("id", json.get("id").asString)
        outputJson.addProperty("rawId", json.get("rawId").asString)
        outputJson.addProperty("type", json.get("type").asString)

        // Create the "extensions" object
        val extensionsObject = JsonObject()
        val credPropsObject = JsonObject()
        extensionsObject.add("credProps", credPropsObject)
        outputJson.add("extensions", extensionsObject)

        // Extract and modify fields in the "response" object
        val responseObject = json.getAsJsonObject("response")
        val finalResponseObject = JsonObject()


        val attestationObject = responseObject.get("authenticatorData")
        finalResponseObject.add("AttestationObject", attestationObject)


        val clientDataJson = responseObject.get("clientDataJSON")
        finalResponseObject.add("clientDataJson", clientDataJson)

        outputJson.add("response",finalResponseObject)
        // Convert the output JSON object to a formatted string
        return gson.toJson(outputJson)
    }

}