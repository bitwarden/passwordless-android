package dev.passwordless.android.rest

import dev.passwordless.android.rest.contracts.*
import retrofit2.Response
import retrofit2.http.*

interface PasswordlessHttpClient {
    /**
     * This endpoint is used to begin the registration process.
     */
    @POST("/register/begin")
    fun registerBegin(@Body inputModel: RegisterBeginRequest): Response<RegisterBeginResponse>

    /**
     * This endpoint is used to complete the registration process.
     * Is generally called after a credential has been successfully created.
     * Stores the public key credential in the RP.
     * @param inputModel
     * @return RegisterCompleteResponse contains the session token
     */
    @POST("/register/complete")
    fun registerComplete(@Body inputModel: RegisterCompleteRequest): Response<RegisterCompleteResponse>
}