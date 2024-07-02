package dev.passwordless.sampleapp.contracts

data class UserRegisterRequest(
    val username:String,
    val alias: String,
    val firstName: String,
    val lastName: String
)
