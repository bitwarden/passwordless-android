package dev.passwordless.android.utils

import android.util.Log
import androidx.credentials.exceptions.CreateCredentialCancellationException
import androidx.credentials.exceptions.CreateCredentialCustomException
import androidx.credentials.exceptions.CreateCredentialException
import androidx.credentials.exceptions.CreateCredentialInterruptedException
import androidx.credentials.exceptions.CreateCredentialProviderConfigurationException
import androidx.credentials.exceptions.CreateCredentialUnknownException
import androidx.credentials.exceptions.publickeycredential.CreatePublicKeyCredentialDomException

class PasswordlessUtils {
    companion object {
        /**
         * These are types of errors that can occur during passkey creation.
         */
        fun getPasskeyFailureMessage(e: Exception): String {
            val message = when (e) {
                is CreatePublicKeyCredentialDomException -> {
                    "An error occurred while creating a passkey"
                }
                is CreateCredentialCancellationException -> {
                    "The user intentionally canceled the operation and chose not to register the credential."
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
}