package dev.passwordless.android.rest.serializers

import androidx.credentials.CreatePublicKeyCredentialResponse
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.google.gson.stream.JsonReader
import java.io.StringReader
import java.lang.reflect.Type


class CreatePublicKeyCredentialResponseSerializer :
    JsonSerializer<CreatePublicKeyCredentialResponse> {
    override fun serialize(
        src: CreatePublicKeyCredentialResponse,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        val reader = JsonReader(StringReader(src.registrationResponseJson));
        return JsonParser.parseReader(reader)
    }
}