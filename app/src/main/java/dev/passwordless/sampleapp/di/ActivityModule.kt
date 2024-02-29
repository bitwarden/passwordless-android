package dev.passwordless.sampleapp.di

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import dev.passwordless.android.PasswordlessClient
import dev.passwordless.android.rest.PasswordlessOptions
import dev.passwordless.sampleapp.auth.Session
import dev.passwordless.sampleapp.config.DemoPasswordlessOptions
import dev.passwordless.sampleapp.yourbackend.YourBackendHttpClient
import dev.passwordless.sampleapp.yourbackend.YourBackendHttpClientFactory
import javax.inject.Singleton

@Module
@InstallIn(ActivityComponent::class)
class ActivityModule {
    @Provides
    fun provideLifecycleCoroutineScope(activity: Activity): LifecycleCoroutineScope =
        (activity as AppCompatActivity).lifecycleScope


    @Provides
    @ActivityScoped
    fun providePasswordlessClient(
        @ActivityContext activity: Context, scope: LifecycleCoroutineScope
    ): PasswordlessClient {
        val options = PasswordlessOptions(
            DemoPasswordlessOptions.API_KEY,
            DemoPasswordlessOptions.RP_ID,
            DemoPasswordlessOptions.ORIGIN,
            DemoPasswordlessOptions.API_URL
        )

        return PasswordlessClient(options, activity, scope)
    }

    @Provides
    @ActivityScoped
    fun provideRetrofitClient(session: Session): YourBackendHttpClient {
        return YourBackendHttpClientFactory.create(DemoPasswordlessOptions.YOUR_BACKEND_URL, session)
    }

    @Provides
    @ActivityScoped
    fun provideSession(@ActivityContext activity: Context): Session {
        return Session(activity!!.applicationContext)
    }
}