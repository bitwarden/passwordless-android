package dev.passwordless.sampleapp.di

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.FragmentScoped
import dev.passwordless.android.PasswordlessClient
import dev.passwordless.android.rest.PasswordlessOptions
import dev.passwordless.sampleapp.auth.Session
import dev.passwordless.sampleapp.config.DemoPasswordlessOptions
import dev.passwordless.sampleapp.yourbackend.YourBackendHttpClient
import dev.passwordless.sampleapp.yourbackend.YourBackendHttpClientFactory

@Module
@InstallIn(FragmentComponent::class)
class FragmentModule {
    @Provides
    fun provideLifecycleCoroutineScope(activity: Activity): LifecycleCoroutineScope =
        (activity as AppCompatActivity).lifecycleScope


    @Provides
    @FragmentScoped
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
    @FragmentScoped
    fun provideRetrofitClient(session: Session): YourBackendHttpClient {
        return YourBackendHttpClientFactory.create(DemoPasswordlessOptions.YOUR_BACKEND_URL, session)
    }

    @Provides
    @FragmentScoped
    fun provideSession(@ActivityContext activity: Context): Session {
        return Session(activity!!.applicationContext)
    }
}