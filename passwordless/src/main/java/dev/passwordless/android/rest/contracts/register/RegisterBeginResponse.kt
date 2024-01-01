package dev.passwordless.android.rest.contracts.register

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName

data class RegisterBeginResponse(
    @SerializedName("data")
    val data: JsonObject,

    @SerializedName("session")
    val session: String
)

