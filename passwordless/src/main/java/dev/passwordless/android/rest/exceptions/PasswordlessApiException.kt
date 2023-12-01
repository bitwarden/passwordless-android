package dev.passwordless.android.rest.exceptions

class PasswordlessApiException(val details: ProblemDetails): Exception(details.title)
