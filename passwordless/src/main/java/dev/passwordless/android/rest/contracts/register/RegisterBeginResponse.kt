package dev.passwordless.android.rest.contracts.register

import androidx.credentials.CreatePublicKeyCredentialRequest
import androidx.credentials.GetPublicKeyCredentialOption
import androidx.credentials.webauthn.PublicKeyCredentialCreationOptions
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName

data class RegisterBeginResponse(
    @SerializedName("data")
    val data: CreatePublicKeyCredentialRequest,

    @SerializedName("session")
    val session: String
)

