package com.example.myapplication.services.yourbackend

import com.example.myapplication.services.yourbackend.contracts.RegisterTokenResponse
import com.example.myapplication.services.yourbackend.contracts.VerifiedUserResponse
import retrofit2.*
import retrofit2.http.*

interface YourBackendHttpClient {
        @GET("/create-token")
        suspend fun register(@Query("alias") alias: String): Response<RegisterTokenResponse>

        @POST("/verify-signin")
        suspend fun login(@Query("token") token: String): Response<VerifiedUserResponse>
}