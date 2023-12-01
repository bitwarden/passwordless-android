package dev.passwordless.android.rest.converters

import java.util.Base64

object Base64UrlConverter {
    fun convert(input: String): ByteArray {
        return Base64.getUrlDecoder().decode(input)
    }

    fun convert(input: ByteArray): String {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(input)
    }
}