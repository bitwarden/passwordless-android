package dev.passwordless.android.rest.contracts

import com.google.android.gms.fido.fido2.api.common.PublicKeyCredentialParameters
import com.google.gson.annotations.SerializedName

data class CredentialCreateOptions(
    @SerializedName("challenge")
    var challenge: ByteArray = byteArrayOf(),

    @SerializedName("timeout")
    var timeout: Long = 60000L,

    @SerializedName("rp")
    val rp: RP,

    @SerializedName("user")
    val user: User,

    @SerializedName("pubKeyCredParams")
    val pubKeyCredParams: List<PubKeyCredParam>,

    @SerializedName("attestation")
    val attestation: String,

    @SerializedName("authenticatorSelection")
    val authenticatorSelection: AuthenticatorSelection,

    @SerializedName("excludeCredentials")
    val excludeCredentials: List<Any>,

    @SerializedName("extensions")
    val extensions: Extensions,

    @SerializedName("status")
    val status: String,

    @SerializedName("errorMessage")
    val errorMessage: String,

    @SerializedName("session")
    val session: String
) {
    data class RP(
        @SerializedName("id")
        val id: String,

        @SerializedName("name")
        val name: String
    )

    data class User(
        @SerializedName("name")
        val name: String,

        @SerializedName("id")
        val id: ByteArray,

        @SerializedName("displayName")
        val displayName: String
    )

    data class PubKeyCredParam(
        @SerializedName("type")
        val type: String,

        @SerializedName("alg")
        val alg: Int
    )

    data class AuthenticatorSelection(
        @SerializedName("residentKey")
        val residentKey: String,

        @SerializedName("requireResidentKey")
        val requireResidentKey: Boolean,

        @SerializedName("userVerification")
        val userVerification: String
    )

    data class Extensions(
        @SerializedName("credProps")
        val credProps: Boolean
    )
}
fun CredentialCreateOptions.getPublicKeyCredentialParameters(): List<PublicKeyCredentialParameters> {
    return pubKeyCredParams.map {
        PublicKeyCredentialParameters(it.type,it.alg)
    }
}
