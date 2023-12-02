package dev.passwordless.android.rest.converters

import java.util.Base64

/**
 * Converts a Base64Url string to a ByteArray and vice versa.
 */
object Base64UrlConverter {
    fun convert(input: String): ByteArray {
        return Base64.getUrlDecoder().decode(input)
    }

    fun convert(input: ByteArray): String {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(input)
    }
}
