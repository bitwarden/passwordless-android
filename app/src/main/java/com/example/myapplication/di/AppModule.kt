package com.example.myapplication.di

import com.example.myapplication.services.yourbackend.YourBackendHttpClientFactory
import com.example.myapplication.services.yourbackend.config.DemoPasswordlessOptions
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.passwordless.android.PasswordlessClient
import dev.passwordless.android.rest.PasswordlessOptions
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // Provided a singleton object of PasswordlessClient

    @Provides
    @Singleton
    fun providePasswordlessClient(): PasswordlessClient {
        val options = PasswordlessOptions(
            DemoPasswordlessOptions.API_KEY,
            DemoPasswordlessOptions.RP_ID,
            DemoPasswordlessOptions.ORIGIN,
            DemoPasswordlessOptions.API_URL
        )
        return PasswordlessClient(options)
    }

    @Provides
    @Singleton
    fun provideRetrofitClient() =
        YourBackendHttpClientFactory.create(DemoPasswordlessOptions.YOUR_BACKEND_URL)
}