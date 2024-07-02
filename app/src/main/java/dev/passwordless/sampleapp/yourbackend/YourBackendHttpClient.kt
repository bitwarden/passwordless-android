package dev.passwordless.sampleapp.yourbackend

import dev.passwordless.sampleapp.contracts.AddCredentialRequest
import dev.passwordless.sampleapp.contracts.AddCredentialResponse
import dev.passwordless.sampleapp.contracts.CredentialResponse
import dev.passwordless.sampleapp.contracts.RegisterTokenResponse
import dev.passwordless.sampleapp.contracts.UserLoginRequest
import dev.passwordless.sampleapp.contracts.UserRegisterRequest
import dev.passwordless.sampleapp.contracts.VerifiedUserResponse
import retrofit2.*
import retrofit2.http.*

interface YourBackendHttpClient {
    @POST("/auth/register")
    suspend fun register(@Body request: UserRegisterRequest): Response<RegisterTokenResponse>

    @POST("/auth/login")
    suspend fun login(@Body request: UserLoginRequest): Response<VerifiedUserResponse>

    @GET("/users/{userId}/credentials")
    suspend fun getCredentials(@Path("userId") userId: String): Response<List<CredentialResponse>>

    @POST("/users/{userId}/credentials")
    suspend fun addCredential(@Path("userId") userId: String, @Body request: AddCredentialRequest): Response<AddCredentialResponse>
}