package dev.passwordless.android.rest.contracts.login


import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName

data class LoginCompleteRequest(
    @SerializedName("session")
    val session: String,

    @SerializedName("response")
    val response: JsonObject,

    @SerializedName("origin")
    val origin: String,

    @SerializedName("rpId")
    val rpId: String
)
