package dev.passwordless.android.rest.serializers

import androidx.credentials.CreatePublicKeyCredentialRequest
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

/**
 * Deserializer for create public key credential requests.
 */
class CreatePublicKeyCredentialRequestDeserializer :
    JsonDeserializer<CreatePublicKeyCredentialRequest> {

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?,
    ): CreatePublicKeyCredentialRequest {
        return CreatePublicKeyCredentialRequest(json.toString())
    }
}
