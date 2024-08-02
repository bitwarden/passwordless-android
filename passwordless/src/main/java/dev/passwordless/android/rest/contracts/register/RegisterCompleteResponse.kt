package dev.passwordless.android.rest.contracts.register

/**
 * The response model for the register complete endpoint.
 *
 * @property token The token for the user.
 */
data class RegisterCompleteResponse(var token: String)
