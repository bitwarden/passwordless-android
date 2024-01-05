package com.example.myapplication.services.yourbackend

import com.example.myapplication.services.yourbackend.contracts.RegisterTokenResponse
import com.example.myapplication.services.yourbackend.contracts.UserLoginRequest
import com.example.myapplication.services.yourbackend.contracts.UserRegisterRequest
import com.example.myapplication.services.yourbackend.contracts.VerifiedUserResponse
import retrofit2.*
import retrofit2.http.*

interface YourBackendHttpClient {
    @POST("/users/register")
    @Headers("X-Pinggy-No-Screen:true")
    suspend fun register(@Body request: UserRegisterRequest): Response<RegisterTokenResponse>

    @POST("/users/login")
    suspend fun login(@Body request: UserLoginRequest): Response<VerifiedUserResponse>
}