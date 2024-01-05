package dev.passwordless.android.rest.contracts.login


import androidx.credentials.GetPublicKeyCredentialOption
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName

data class LoginBeginResponse(
    @SerializedName("data")
    val data: GetPublicKeyCredentialOption,
    @SerializedName("session")
    val session: String
)