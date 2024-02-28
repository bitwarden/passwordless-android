package dev.passwordless.sampleapp.contracts

import java.time.LocalDateTime

data class CredentialResponse (
    val descriptor: CredentialDescriptor,
    val publicKey: String,
    val userHandle: String,
    val signatureCounter: Int,
    val attestationFmt: String,
    val createdAt: String,
    val aaGuid: String,
    val lastUsedAt: String,
    val rpid: String,
    val origin: String,
    val country: String,
    val device: String,
    val nickname: String,
    val userId: String
)

data class CredentialDescriptor(
    val id: String
)