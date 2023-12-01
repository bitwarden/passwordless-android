package com.example.myapplication.services.yourbackend.contracts

import android.R.bool
import android.R.string
import java.time.LocalDateTime
import java.util.*


data class VerifiedUserResponse(
    val userId: string,
    val credentialId: ByteArray,
    val success: bool,
    val timestamp: LocalDateTime,
    val rpId: string,
    val origin: string,
    val device: string,
    val country: string,
    val nickname: string,
    val expiresAt: LocalDateTime,
    val tokenId: UUID,
    val type: string)