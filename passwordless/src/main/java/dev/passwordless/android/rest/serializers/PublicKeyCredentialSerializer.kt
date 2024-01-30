package dev.passwordless.android.rest.serializers

import androidx.credentials.PublicKeyCredential
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.google.gson.stream.JsonReader
import java.io.StringReader
import java.lang.reflect.Type

class PublicKeyCredentialSerializer :
    JsonSerializer<PublicKeyCredential> {
    override fun serialize(
        src: PublicKeyCredential,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        val reader = JsonReader(StringReader(src.authenticationResponseJson));
        return JsonParser.parseReader(reader)
    }
}