package dev.passwordless.android.rest.contracts.register

import androidx.credentials.CreatePublicKeyCredentialRequest
import com.google.gson.annotations.SerializedName

/**
 * The response model for the register begin endpoint.
 *
 * @property data The data for the registration.
 * @property session The session ID for the registration.
 */
data class RegisterBeginResponse(
    @SerializedName("data")
    val data: CreatePublicKeyCredentialRequest,

    @SerializedName("session")
    val session: String,
)
