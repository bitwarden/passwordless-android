package dev.passwordless.android.utils

import androidx.credentials.exceptions.CreateCredentialCancellationException
import androidx.credentials.exceptions.CreateCredentialCustomException
import androidx.credentials.exceptions.CreateCredentialInterruptedException
import androidx.credentials.exceptions.CreateCredentialProviderConfigurationException
import androidx.credentials.exceptions.CreateCredentialUnknownException
import androidx.credentials.exceptions.publickeycredential.CreatePublicKeyCredentialDomException

/**
 * Utility class for Passwordless.
 */
object PasswordlessUtils {
    /**
     * These are types of errors that can occur during passkey creation.
     */
    fun getPasskeyFailureMessage(e: Exception): String {
        val message = when (e) {
            is CreatePublicKeyCredentialDomException -> {
                "An error occurred while creating a passkey"
            }
            is CreateCredentialCancellationException -> {
                "The operation to register a credential was intentionally canceled by the user."
            }
            is CreateCredentialInterruptedException -> {
                "The operation was interrupted, please retry the call."
            }
            is CreateCredentialProviderConfigurationException -> {
                "Your app is missing the provider configuration dependency."
            }
            is CreateCredentialUnknownException -> {
                "An unknown error occurred while creating passkey."
            }
            is CreateCredentialCustomException -> {
                "An unknown error occurred from a 3rd party SDK."
            }
            else -> {
                e.message ?: "An unknown error occurred."
            }
        }
        return message
    }
}
