package dev.passwordless.android.rest.contracts

import com.google.gson.annotations.SerializedName

data class RegisterBeginRequest(
    @SerializedName("token")
    val token: String,

    @SerializedName("RPID")
    val rpId: String,

    @SerializedName("Origin")
    val origin: String
)