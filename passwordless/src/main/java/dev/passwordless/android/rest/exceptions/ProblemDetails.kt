package dev.passwordless.android.rest.exceptions

/**
 * Represents the details of a problem that occurred.
 *
 * @property type The type of the problem.
 * @property title The title of the problem.
 * @property status The HTTP status code of the problem.
 * @property detail The detailed description of the problem.
 * @property instance The instance of the problem.
 */
data class ProblemDetails(
    val type: String,
    val title: String,
    val status: Int,
    val detail: String? = null,
    val instance: String? = null,
)
