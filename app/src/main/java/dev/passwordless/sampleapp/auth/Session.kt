package dev.passwordless.sampleapp.auth

import android.content.Context
import android.content.Context.MODE_MULTI_PROCESS
import android.content.Context.MODE_PRIVATE
import androidx.preference.PreferenceManager
import com.auth0.android.jwt.JWT

data class Session(val context: Context) {
    fun isLoggedIn(): Boolean {
        return getJwt() != null && !getJwt()!!.isExpired(60)
    }

    fun getUserId(): String? {
        return getJwt()?.getClaim("nameid")?.asString() ?: null
    }

    fun getUsername(): String? {
        return getJwt()?.getClaim("unique_name")?.asString() ?: null
    }

    fun setJwtString(jwt: String) {
        val sharedPreferences = context.getSharedPreferences("pwdemo", MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString("jwt", jwt)
            commit()
        }
    }

    fun getJwtString(): String? {
        val sharedPreferences = context.getSharedPreferences("pwdemo", MODE_PRIVATE)
        with(sharedPreferences) {
            return getString("jwt", null)
        }
    }

    private fun getJwt(): JWT? {
        val jwt = getJwtString()
        if (jwt == null) {
            return null
        } else {
            return JWT(jwt)
        }
    }
}