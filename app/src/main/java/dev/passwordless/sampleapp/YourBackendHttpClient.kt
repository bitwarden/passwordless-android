package dev.passwordless.sampleapp

import retrofit2.*
import retrofit2.http.*

interface YourBackendHttpClient {
    @POST("/auth/register")
    suspend fun register(@Body request: UserRegisterRequest): Response<RegisterTokenResponse>

    @POST("/auth/login")
    suspend fun login(@Body request: UserLoginRequest): Response<VerifiedUserResponse>
}