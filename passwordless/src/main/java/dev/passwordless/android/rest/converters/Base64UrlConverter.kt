package dev.passwordless.android.rest.converters

import android.util.Base64

/**
 * A converter for Base64 URL encoding.
 */
object Base64UrlConverter {
    private const val FLAGS: Int = Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP

    /**
     * Converts a Base64 URL encoded string to a byte array.
     *
     * @param input The Base64 URL encoded string.
     * @return The byte array.
     */
    fun convert(input: String): ByteArray {
        return Base64.decode(input, FLAGS)
    }

    /**
     * Converts a byte array to a Base64 URL encoded string.
     *
     * @param input The byte array.
     * @return The Base64 URL encoded string.
     */
    fun convert(input: ByteArray): String {
        return Base64.encodeToString(input, FLAGS)
    }
}
