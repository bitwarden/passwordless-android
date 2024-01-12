package dev.passwordless.android.rest.serializers

import androidx.credentials.CreatePublicKeyCredentialResponse
import com.google.gson.JsonElement
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import dev.passwordless.android.rest.converters.PublicKeyCredentialConverter
import java.lang.reflect.Type

class CreatePublicKeyCredentialResponseSerializer :
    JsonSerializer<CreatePublicKeyCredentialResponse> {
    override fun serialize(
        src: CreatePublicKeyCredentialResponse,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return PublicKeyCredentialConverter.convertJson(src.registrationResponseJson)
    }
}