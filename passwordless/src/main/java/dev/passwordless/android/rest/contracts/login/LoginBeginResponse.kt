package dev.passwordless.android.rest.contracts.login


import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName

data class LoginBeginResponse(
    @SerializedName("data")
    val data: JsonObject,
    @SerializedName("session")
    val session: String
)