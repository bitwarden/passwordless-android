package com.example.myapplication.services.yourbackend.contracts


import com.google.gson.annotations.SerializedName

data class VerifiedUserResponse(
    @SerializedName("userId") val userId: String,
    @SerializedName("credentialId") val credentialId: String,
    @SerializedName("success") val success: Boolean,
    @SerializedName("timestamp") val timestamp: String,
    @SerializedName("rpId") val rpId: String,
    @SerializedName("origin") val origin: String,
    @SerializedName("device") val device: String,
    @SerializedName("country") val country: String,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("expiresAt") val expiresAt: String,
    @SerializedName("tokenId") val tokenId: String,
    @SerializedName("type") val type: String
)

