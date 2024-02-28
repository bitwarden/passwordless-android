package dev.passwordless.sampleapp.contracts


import com.google.gson.annotations.SerializedName

data class VerifiedUserResponse(
    @SerializedName("jwtToken") val jwtToken: String
)

