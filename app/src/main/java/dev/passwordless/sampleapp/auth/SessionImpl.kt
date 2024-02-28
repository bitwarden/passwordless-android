package dev.passwordless.sampleapp.auth

import com.auth0.android.jwt.JWT

data class SessionImpl(override val jwt: String?) : Session {
    val userId: String?

    init {
        if (jwt != null) {
            val jwtObj = JWT(jwt!!)
            userId = jwtObj.getClaim("nameid").asString()
        } else {
            userId = null
        }
    }

    override fun isLoggedIn(): Boolean {
        return jwt != null && userId != null
    }
}