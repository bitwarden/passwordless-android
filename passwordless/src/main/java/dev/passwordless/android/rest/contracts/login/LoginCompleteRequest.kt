package dev.passwordless.android.rest.contracts.login

import androidx.credentials.PublicKeyCredential
import com.google.gson.annotations.SerializedName

/**
 * The request model for the login complete endpoint.
 *
 * @property session The session ID for the login.
 * @property response The response from the authenticator.
 * @property origin The origin of the request.
 * @property rpId The identifier of the relying party.
 */
data class LoginCompleteRequest(
    @SerializedName("session")
    val session: String,

    @SerializedName("response")
    val response: PublicKeyCredential,

    @SerializedName("origin")
    val origin: String,

    @SerializedName("rpId")
    val rpId: String,
)
