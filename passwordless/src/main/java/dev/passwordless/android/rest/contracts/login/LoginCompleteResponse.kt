package dev.passwordless.android.rest.contracts.login

import com.google.gson.annotations.SerializedName

data class LoginCompleteResponse(
    @SerializedName("token")
    val token: String
)
