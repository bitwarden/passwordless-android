package dev.passwordless.android.rest.exceptions

/**
 * Represents an exception thrown by the Passwordless API containing details about the error.
 *
 * @property details The details of the exception.
 */
class PasswordlessApiException(val details: ProblemDetails) : Exception(details.title)
