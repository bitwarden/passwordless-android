package dev.passwordless.android.rest.contracts

import com.google.gson.annotations.SerializedName

data class AuthenticatorAttestationRawResponse(
    @SerializedName("id")
    val id: ByteArray,

    @SerializedName("rawId")
    val rawId: ByteArray,

    @SerializedName("type")
    val type: String,

    @SerializedName("response")
    val response: ResponseData,

    @SerializedName("extensions")
    val extensions: AuthenticationExtensionsClientOutputs,
    )

data class ResponseData(
    @SerializedName("attestationObject")
    val attestationObject: ByteArray,

    @SerializedName("clientDataJson")
    val clientDataJSON: ByteArray)

data class AuthenticationExtensionsClientOutputs(
    @SerializedName("appid")
    val appId: String? = null,

    @SerializedName("authnSel")
    val authenticatorSelection: Boolean? = null,

    @SerializedName("exts")
    val exts: Array<String>? = null,

    @SerializedName("uvm")
    val uvm: Array<Array<ULong>>? = null,

    @SerializedName("devicePubKey")
    val devicePubKey: AuthenticationExtensionsDevicePublicKeyOutputs? = null,

    @SerializedName("credProps")
    val credProps: CredentialPropertiesOutput? = null,

    @SerializedName("prf")
    val prf: AuthenticationExtensionsPRFOutputs? = null
)

data class AuthenticationExtensionsDevicePublicKeyOutputs(
    @SerializedName("authenticatorOutput")
    val authenticatorOutput: ByteArray,

    @SerializedName("signature")
    val signature: ByteArray
)

data class CredentialPropertiesOutput(
    @SerializedName("rk")
    val rk: Boolean)

data class AuthenticationExtensionsPRFOutputs(
    @SerializedName("enabled")
    val enabled: Boolean,

    @SerializedName("results")
    val results: AuthenticationExtensionsPRFValues)

data class AuthenticationExtensionsPRFValues(
    @SerializedName("first")
    val first: ByteArray,

    @SerializedName("second")
    val second: ByteArray
)