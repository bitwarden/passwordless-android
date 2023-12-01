package dev.passwordless.android.rest.contracts

import com.google.gson.annotations.SerializedName

data class CredentialCreateOptions(
    @SerializedName("challenge")
    val challenge: ByteArray = byteArrayOf(),

    @SerializedName("timeout")
    val timeout: Long = 60000L,

    @SerializedName("rpId")
    val rpId: String?,

    @SerializedName("userVerification")
    val userVerification: String? = null,

    @SerializedName("status")
    val status: String?,

    @SerializedName("errorMessage")
    val errorMessage: String?) {

}