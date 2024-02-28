package dev.passwordless.sampleapp.auth

interface Session {
    val jwt: String?

    fun isLoggedIn(): Boolean;
}