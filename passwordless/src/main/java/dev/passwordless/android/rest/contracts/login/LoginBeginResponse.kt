package dev.passwordless.android.rest.contracts.login

import androidx.credentials.GetPublicKeyCredentialOption
import com.google.gson.annotations.SerializedName

/**
 * Response for the login begin endpoint.
 */
data class LoginBeginResponse(
    @SerializedName("data")
    val data: GetPublicKeyCredentialOption,
    @SerializedName("session")
    val session: String,
)
