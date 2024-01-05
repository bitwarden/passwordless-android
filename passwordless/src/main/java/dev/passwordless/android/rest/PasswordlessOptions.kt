package dev.passwordless.android.rest

import dev.passwordless.android.rest.converters.Base64UrlConverter

/**
 * @property apiUrl The Passwordless.dev server url.
 * @property apiKey Your public API key.
 * @property rpId This stands for “relying party”; it can be considered as describing the organization responsible for registering and authenticating the user.
 * @property origin This is where your backend is hosted.
 */
data class PasswordlessOptions(
    val apiKey: String,
    val rpId: String,
    val origin: String,
    val apiUrl: String = "https://v4.passwordless.dev"
) {init {
    require(apiKey.isNotBlank()) { "apiKey must not be blank" }
    require(isValidApiKey(apiKey)) { "apiKey must be a valid API key '<appname>:public:<uuid-without-dashes>'" }
    require(rpId.isNotBlank()) { "rpId must not be blank" }
    require(isFacetId(origin)) { "origin must be a facet id 'android:apk-key-hash:base64url'" }
    require(isValidOrigin(origin)) { "origin must be a valid URL" }
}

    private fun isValidApiKey(apiKey: String): Boolean {
        // Split the apiKey by ':'
        val segments = apiKey.split(':')

        // Check if there are exactly three segments
        if (segments.size != 3) {
            return false
        }

        // Check if the middle segment is "public"
        if (segments[1] != "public") {
            return false
        }

        // Check if the last segment is of the correct length (32 characters)
        if (segments[2].length != 32) {
            return false
        }

        // If all checks pass, the apiKey is valid
        return true
    }

    private fun isFacetId(origin: String): Boolean {
        return origin.startsWith("android:apk-key-hash:")
    }

    private fun isValidOrigin(origin: String): Boolean {
        return Base64UrlConverter.isValid(origin.substringAfter("android:apk-key-hash:"))
    }
}
