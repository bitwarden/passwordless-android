package dev.passwordless.sampleapp.auth

import com.auth0.android.jwt.JWT

data class Session(val jwt: String?) {
    lateinit var jwtToken: JWT
    lateinit var userId: String

    init {
        if (jwt != null) {
            jwtToken = JWT(jwt)
            userId = jwtToken.getClaim("nameid").asString()!!
        }
    }

    fun isLoggedIn(): Boolean {
        return jwt != null
    }
}