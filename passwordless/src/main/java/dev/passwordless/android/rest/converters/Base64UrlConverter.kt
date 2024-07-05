package dev.passwordless.android.rest.converters

import android.util.Base64

object Base64UrlConverter {
    private const val flags: Int = Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP

    fun convert(input: String): ByteArray {
        return Base64.decode(input, flags)
    }

    fun convert(input: ByteArray): String {
        return Base64.encodeToString(input, flags)
    }
}