package dev.passwordless.android.rest

import dev.passwordless.android.rest.contracts.*
import dev.passwordless.android.rest.contracts.login.LoginBeginRequest
import dev.passwordless.android.rest.contracts.login.LoginBeginResponse
import dev.passwordless.android.rest.contracts.login.LoginCompleteRequest
import dev.passwordless.android.rest.contracts.login.LoginCompleteResponse
import dev.passwordless.android.rest.contracts.register.RegisterBeginRequest
import dev.passwordless.android.rest.contracts.register.RegisterBeginResponse
import dev.passwordless.android.rest.contracts.register.RegisterCompleteRequest
import dev.passwordless.android.rest.contracts.register.RegisterCompleteResponse
import retrofit2.Response
import retrofit2.http.*

interface PasswordlessHttpClient {
    /**
     * This endpoint is used to begin the registration process.
     */
    @POST("/register/begin")
    suspend fun registerBegin(@Body inputModel: RegisterBeginRequest): Response<RegisterBeginResponse>

    /**
     * This endpoint is used to complete the registration process.
     * Is generally called after a credential has been successfully created.
     * Stores the public key credential in the RP.
     * @param inputModel
     * @return RegisterCompleteResponse contains the session token
     */
    @POST("/register/complete")
    suspend fun registerComplete(@Body inputModel: RegisterCompleteRequest): Response<RegisterCompleteResponse>
    /**
     * This endpoint is used to begin the login process.
     * Initiates the login process for a user with the provided alias, RP ID, and origin.
     *
     * @param inputModel The [LoginBeginRequest] containing the necessary information for login initiation.
     * @return A [Response] containing the [LoginBeginResponse] with session and data information.
     */
    @POST("/signin/begin")
    suspend fun loginBegin(@Body inputModel: LoginBeginRequest): Response<LoginBeginResponse>
    /**
     * This endpoint is used to complete the login process.
     * Is called after a user has provided their credentials.
     *
     * @param inputModel The [LoginCompleteRequest] containing the session, response, RP ID, and origin.
     * @return A [Response] containing the [LoginCompleteResponse] with session token information.
     */
    @POST("/signin/complete")
    suspend fun loginComplete(@Body inputModel: LoginCompleteRequest): Response<LoginCompleteResponse>
}
