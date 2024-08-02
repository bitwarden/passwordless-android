package dev.passwordless.android.rest.contracts.register

import com.google.gson.annotations.SerializedName

/**
 * The request model for the register begin endpoint.
 *
 * @property token The token used to identify the user.
 * @property rpId The RP ID of the relying party.
 * @property origin The origin of the request.
 */
data class RegisterBeginRequest(
    @SerializedName("token")
    val token: String,

    @SerializedName("RPID")
    val rpId: String,

    @SerializedName("Origin")
    val origin: String,
)
