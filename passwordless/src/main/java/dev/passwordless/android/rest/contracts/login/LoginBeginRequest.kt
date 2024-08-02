package dev.passwordless.android.rest.contracts.login

import com.google.gson.annotations.SerializedName

/**
 * The request model for the login begin endpoint.
 *
 * @property alias The alias of the user.
 * @property rpId The identifier of the relying party.
 * @property origin The origin of the request.
 */
data class LoginBeginRequest(
    @SerializedName("alias")
    val alias: String,

    @SerializedName("RPID")
    val rpId: String,

    @SerializedName("Origin")
    val origin: String,
)
