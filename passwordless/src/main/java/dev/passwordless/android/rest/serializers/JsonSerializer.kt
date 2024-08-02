package dev.passwordless.android.rest.serializers

import com.google.gson.Gson

/**
 * Interface for JSON serializers.
 */
interface JsonSerializer<T> {
    /**
     * Gets the JSON serializer.
     */
    fun get(): Gson
}
