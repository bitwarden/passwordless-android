package dev.passwordless.android.rest.serializers

import androidx.credentials.GetPublicKeyCredentialOption
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

/**
 * Deserializer for get public key credential options.
 */
class GetPublicKeyCredentialOptionDeserializer : JsonDeserializer<GetPublicKeyCredentialOption> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?,
    ): GetPublicKeyCredentialOption {
        return GetPublicKeyCredentialOption(json.toString(), null)
    }
}
