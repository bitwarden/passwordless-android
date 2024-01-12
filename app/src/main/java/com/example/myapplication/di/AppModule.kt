package com.example.myapplication.di

import android.content.Context
import com.example.myapplication.services.yourbackend.YourBackendHttpClientFactory
import com.example.myapplication.services.yourbackend.config.DemoPasswordlessOptions
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import dev.passwordless.android.PasswordlessClient
import dev.passwordless.android.rest.PasswordlessOptions

@Module
@InstallIn(ActivityComponent::class)
class AppModule {
    @Provides
    @ActivityScoped
    fun providePasswordlessClient(@ActivityContext activity: Context): PasswordlessClient {
        val options = PasswordlessOptions(
            DemoPasswordlessOptions.API_KEY,
            DemoPasswordlessOptions.RP_ID,
            DemoPasswordlessOptions.ORIGIN,
            DemoPasswordlessOptions.API_URL
        )
        return PasswordlessClient(options, activity)
    }

    @Provides
    @ActivityScoped
    fun provideRetrofitClient() =
        YourBackendHttpClientFactory.create(DemoPasswordlessOptions.YOUR_BACKEND_URL)
}