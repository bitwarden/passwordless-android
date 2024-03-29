package dev.passwordless.android.rest.contracts.register


import androidx.credentials.CreatePublicKeyCredentialResponse
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName

data class RegisterCompleteRequest(
    @SerializedName("session")
    val session: String,

    @SerializedName("response")
    val response: CreatePublicKeyCredentialResponse,

    @SerializedName("nickname")
    val nickname: String?,

    @SerializedName("origin")
    val origin: String,

    @SerializedName("rpId")
    val rpId: String
)
