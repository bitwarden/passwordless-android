package dev.passwordless.android.rest.exceptions

data class ProblemDetails(
    val type: String,
    val title: String,
    val status: Int,
    val detail: String? = null,
    val instance: String? = null,
)