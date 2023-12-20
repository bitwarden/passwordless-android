package dev.passwordless.android.rest.converters

import java.util.Base64

object Base64UrlConverter {
    private val regex = Regex("^[A-Za-z0-9_-]*$")

    fun convert(input: String): ByteArray {
        return Base64.getUrlDecoder().decode(input)
    }

    fun convert(input: ByteArray): String {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(input)
    }


    /**
     * Checks if the input is a valid Base64 URL-encoded string.
     * @param input The input to check.
     * @return True if the input is a valid Base64 URL-encoded string, false otherwise.
     */
    fun isValid(input: String): Boolean {
        return regex.matches(input)
    }
}