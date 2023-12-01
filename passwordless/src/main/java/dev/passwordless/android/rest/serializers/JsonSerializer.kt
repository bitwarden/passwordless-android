package dev.passwordless.android.rest.serializers

import com.google.gson.Gson

interface JsonSerializer {
    fun get(): Gson
}