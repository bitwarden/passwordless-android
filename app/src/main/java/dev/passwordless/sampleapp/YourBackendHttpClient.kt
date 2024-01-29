package dev.passwordless.sampleapp

import retrofit2.*
import retrofit2.http.*

interface YourBackendHttpClient {
    @POST("/users/register")
    @Headers("X-Pinggy-No-Screen:true")
    suspend fun register(@Body request: UserRegisterRequest): Response<RegisterTokenResponse>

    @POST("/users/login")
    suspend fun login(@Body request: UserLoginRequest): Response<VerifiedUserResponse>
}