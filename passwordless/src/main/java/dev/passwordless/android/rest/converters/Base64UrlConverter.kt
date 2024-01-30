package dev.passwordless.android.rest.converters

import android.util.Base64

object Base64UrlConverter {
    private val regex = Regex("^[A-Za-z0-9_-]*$")

    fun convert(input: String): ByteArray {
        return Base64.decode(input, Base64.URL_SAFE)
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