package dev.passwordless.android.rest.contracts

import com.google.android.gms.fido.fido2.api.common.PublicKeyCredential
import com.google.gson.annotations.SerializedName

data class RegisterCompleteRequest (
    @SerializedName("session")
    val session: String,

    @SerializedName("response")
    val response: PublicKeyCredential,

    @SerializedName("nickname")
    val nickname: String?,

    @SerializedName("origin")
    val origin: String,

    @SerializedName("rpId")
    val rpId: String
    )
