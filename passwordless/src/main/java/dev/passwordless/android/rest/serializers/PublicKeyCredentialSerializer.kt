package dev.passwordless.android.rest.serializers

import androidx.credentials.CreatePublicKeyCredentialResponse
import androidx.credentials.PublicKeyCredential
import com.google.gson.JsonElement
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import dev.passwordless.android.rest.converters.PublicKeyCredentialConverter
import java.lang.reflect.Type

class PublicKeyCredentialSerializer :
    JsonSerializer<PublicKeyCredential> {
    override fun serialize(
        src: PublicKeyCredential,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return PublicKeyCredentialConverter.convertJson(src.authenticationResponseJson)
    }
}