package dev.passwordless.android.rest

/**
 * @property apiUrl The Passwordless.dev server url.
 * @property apiKey Your public API key.
 * @property rpId This stands for “relying party identifier”; it can be considered as describing the
 * organization responsible for registering and authenticating the user.
 */
data class PasswordlessOptions(
    val apiKey: String,
    val rpId: String,
    val apiUrl: String = "https://v4.passwordless.dev",
) { init {
    require(apiKey.isNotBlank()) {
        "apiKey must not be blank"
    }
    require(isValidApiKey(apiKey)) {
        "apiKey must be a valid API key '<appname>:public:<uuid-without-dashes>'"
    }
    require(rpId.isNotBlank()) {
        "rpId must not be blank"
    }
}

    private fun isValidApiKey(apiKey: String): Boolean {
        // Split the apiKey by ':'
        val segments = apiKey.split(':')

        // Check if there are exactly three segments
        if (segments.size != API_KEY_SEGMENTS) {
            return false
        }

        // Check if the middle segment is "public"
        if (segments[1] != "public") {
            return false
        }

        // Check if the last segment is of the correct length (32 characters)
        if (segments[2].length != UUID_LENGTH) {
            return false
        }

        // If all checks pass, the apiKey is valid
        return true
    }

    /**
     * Constants for the Passwordless API key format.
     */
    companion object {
        const val API_KEY_SEGMENTS = 3
        const val UUID_LENGTH = 32
    }
}
