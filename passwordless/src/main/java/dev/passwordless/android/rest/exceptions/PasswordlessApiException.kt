package dev.passwordless.android.rest.exceptions

class PasswordlessApiException(details: ProblemDetails): Exception(details.title)