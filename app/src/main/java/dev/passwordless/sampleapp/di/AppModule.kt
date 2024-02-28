package dev.passwordless.sampleapp.di

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import dagger.hilt.components.SingletonComponent
import dev.passwordless.android.PasswordlessClient
import dev.passwordless.android.rest.PasswordlessOptions
import dev.passwordless.sampleapp.auth.Session
import dev.passwordless.sampleapp.auth.SessionImpl
import dev.passwordless.sampleapp.config.DemoPasswordlessOptions
import dev.passwordless.sampleapp.yourbackend.YourBackendHttpClient
import dev.passwordless.sampleapp.yourbackend.YourBackendHttpClientFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideRetrofitClient(session: Session): YourBackendHttpClient {
        return YourBackendHttpClientFactory.create(DemoPasswordlessOptions.YOUR_BACKEND_URL, session)
    }

    @Provides
    @Singleton
    fun provideSession(): Session =
        SessionImpl(null)
}