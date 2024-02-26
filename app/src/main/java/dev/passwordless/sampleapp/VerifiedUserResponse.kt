package dev.passwordless.sampleapp


import com.google.gson.annotations.SerializedName

data class VerifiedUserResponse(
    @SerializedName("jwtToken") val jwtToken: String
)

