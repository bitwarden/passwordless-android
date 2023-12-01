package dev.passwordless.android.rest.contracts

import com.google.gson.annotations.SerializedName

data class RegisterBeginResponse(
    @SerializedName("data")
    val data: CredentialCreateOptions,

    @SerializedName("session")
    val session: String)

