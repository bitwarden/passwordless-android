package dev.passwordless.android.rest.serializers

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import dev.passwordless.android.rest.converters.Base64UrlConverter
import java.lang.reflect.Type

/**
 * Deserializer for base64url encoded strings.
 */
class Base64UrlDeserializer : JsonDeserializer<ByteArray> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?,
    ): ByteArray {
        if (json == null || json.isJsonNull) {
            return ByteArray(0)
        }

        val base64urlString = json.asString
        return Base64UrlConverter.convert(base64urlString)
    }
}
