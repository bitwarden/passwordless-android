package dev.passwordless.android.rest.contracts.login

import com.google.gson.annotations.SerializedName

data class LoginBeginRequest(
    @SerializedName("alias")
    val alias: String,

    @SerializedName("RPID")
    val rpId: String,

    @SerializedName("Origin")
    val origin: String
)