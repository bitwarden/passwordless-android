package dev.passwordless.sampleapp.auth

import android.content.Context
import androidx.preference.PreferenceManager
import com.auth0.android.jwt.JWT

data class Session(val context: Context) {
    fun isLoggedIn(): Boolean {
        return getJwt() != null
    }

    fun getUserId(): String? {
        return getJwt()?.getClaim("nameid")?.asString() ?: null
    }

    fun getUsername(): String? {
        return getJwt()?.getClaim("unique_name")?.asString() ?: null
    }

    fun getJwtString(): String? {
        return PreferenceManager.getDefaultSharedPreferences(context).getString("jwt", null)
    }

    fun getJwt(): JWT? {
        val jwt = getJwtString()
        if (jwt == null) {
            return null
        } else {
            return JWT(jwt)
        }
    }
}