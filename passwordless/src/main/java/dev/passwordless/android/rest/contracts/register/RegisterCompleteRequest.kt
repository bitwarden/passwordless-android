package dev.passwordless.android.rest.contracts.register

import androidx.credentials.CreatePublicKeyCredentialResponse
import com.google.gson.annotations.SerializedName

/**
 * The request model for the register complete endpoint.
 *
 * @property session The session ID for the registration.
 * @property response The response from the authenticator.
 * @property nickname The nickname of a credential could represent the location where the credential
 * is stored, i.e. "YubiKey Work" or "Marc's Laptop".
 * @property origin The origin of the request.
 * @property rpId The identifier of the relying party.
 */
data class RegisterCompleteRequest(
    @SerializedName("session")
    val session: String,

    @SerializedName("response")
    val response: CreatePublicKeyCredentialResponse,

    @SerializedName("nickname")
    val nickname: String?,

    @SerializedName("origin")
    val origin: String,

    @SerializedName("rpId")
    val rpId: String,
)
