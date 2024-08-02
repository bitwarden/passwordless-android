package dev.passwordless.android.rest.contracts.login

import com.google.gson.annotations.SerializedName

/**
 * The response model for the login complete endpoint.
 *
 * @property token The token for the user.
 */
data class LoginCompleteResponse(
    @SerializedName("token")
    val token: String,
)
