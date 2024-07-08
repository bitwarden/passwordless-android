![Build](https://github.com/bitwarden/passwordless-android/actions/workflows/android.yml/badge.svg)
[![Maven Central](https://img.shields.io/maven-central/v/com.bitwarden/passwordless-android.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22com.bitwarden%22%20AND%20a:%22passwordless-android%22)

# Android Client SDK

The Passwordless.dev Android client SDK gives users the ability to leverage their device’s built-in fingerprint sensor and/or FIDO security keys for secure passwordless access to websites and native applications that support the FIDO2 protocols

## Creating your first Android application

### Requirements

- Android 9.0 (API level 28) or higher
- Java 8 or higher
- [Completed 'Get started' guide](https://docs.passwordless.dev/guide/frontend/android.html).

### Installation

Apache Maven

```xml
<dependency>
  <groupId>com.bitwarden</groupId>
  <artifactId>passwordless-android</artifactId>
  <version>1.1.0</version>
</dependency>
```

Gradle Kotlin DSL

```kotlin
implementation("com.bitwarden:passwordless-android:1.1.0")
```

Gradle Groovy DSL

```groovy
implementation 'com.bitwarden:passwordless-android:1.1.0'
```

### Permissions

When the library has been added to your app, the following permission will be added to your `AndroidManifest.xml` automatically when the app is being built.

It is not necessary for you to add the following permission.

```xml
<uses-permission android:name="android.permission.INTERNET" />
```

### Configuration (Android application)

```kotlin
data class PasswordlessOptions(
   // Your public API key
   val apiKey: String,

   // Identifier for your server, for example 'example.com' if your backend is hosted at https://example.com.
   val rpId: String,

   // Where your backend is hosted
   val backendUrl:String,

   // Passwordless.dev server, change for self-hosting
   val apiUrl: String = "https://v4.passwordless.dev"
)
```

#### .well-known/assetlinks.json

In your application's `AndroidManifest.xml`, you will then need to add the tag below under `manifest::application`:

```xml
<meta-data
            android:name="asset_statements"
            android:resource="@xml/assetlinks" />
```

In your application's `res/xml/assetlinks.xml`, you will then need to add the following content. This will tell our Android application where our `.well-known/assetlinks.json` file is hosted.

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="assetlinks">https://yourexample.com/.well-known/assetlinks.json</string>
</resources>
```

### Configuration (Your back-end)

#### Generating SHA-256 Certificate Fingerprints

To configure your backend, you'll need to host a `.well-known/assetlinks.json` file at the root of your domain. This file contains a list of SHA-256 certificate fingerprints that are allowed to authenticate with your backend.

This command will print detailed information about the keystore entry with the specified alias, including information about the certificate, its validity, and other details. It's commonly used in Android development to verify the properties of the debug keystore and the associated key used for signing applications during development.

- Option 1:
  - MacOS & Linux:
    ```bash
    keytool -list -v -keystore ~/.android/debug.keystore -alias androiddebugkey -storepass android -keypass android
    ```
  - Windows:
    ```powershell
    keytool -list -v -keystore "%USERPROFILE%\.android\debug.keystore" -alias androiddebugkey -storepass android -keypass android
    ```
- Option 2:
  Go to the root directory of the project from the terminal and run the below command
  ```bash
  ./gradlew signingReport
  ```
  Put this SHA256 along with your root android package name in your backend to generate `assetlinks.json` for your app at `https://yourexample.com/.well-known/assetlinks.json`.
  If you are using `example-nodejs-backend`. then just put these 2 values in your `.env` file.

#### Host ~/.well-known/assetlinks.json

You will need store the following file at `https://<your-domain>/.well-known/assetlinks.json`. To generate the SHA256 hash, read the links below the snippet.

You may also have to change the 'target::namespace' and 'target::package_name' properties to match your application's.

```json
[
  {
    "relation": [
      "delegate_permission/common.handle_all_urls",
      "delegate_permission/common.get_login_creds"
    ],
    "target": {
      "namespace": "web"
    }
  },
  {
    "relation": [
      "delegate_permission/common.handle_all_urls",
      "delegate_permission/common.get_login_creds"
    ],
    "target": {
      "namespace": "android_app",
      "package_name": "com.example.myapplication",
      "sha256_cert_fingerprints": [
        "3C:E2:29:94:E2:DE:1E:EB:E5:F9:70:10:72:41:F4:0F:06:38:61:BD:72:76:79:CA:72:68:67:FA:38:9B:65:B9"
      ]
    }
  }
]
```

- [Associate apps & sites - Google](https://developers.google.com/identity/smartlock-passwords/android/associate-apps-and-sites)
- [Passkeys - Google](https://developer.android.com/training/sign-in/passkeys)

### Using the PasswordlessClient

#### With Dagger Hilt

You can either set the `ActivityContext` and `CoroutineScope` by injecting it with Dagger Hilt as follows:

```kotlin
@Module
@InstallIn(ActivityComponent::class)
class PasswordlessModule {
    @Provides
    fun provideLifecycleCoroutineScope(activity: Activity): LifecycleCoroutineScope =
        (activity as AppCompatActivity).lifecycleScope

    @Provides
    @ActivityScoped
    fun providePasswordlessClient(
        @ActivityContext activity: Context, scope: LifecycleCoroutineScope): PasswordlessClient {
        val options = PasswordlessOptions(
            DemoPasswordlessOptions.API_KEY,
            DemoPasswordlessOptions.RP_ID,
            DemoPasswordlessOptions.ORIGIN,
            DemoPasswordlessOptions.API_URL
        )

        return PasswordlessClient(options, activity, scope)
    }
}
```

#### Without Dagger Hilt

Or you can set the Context of PasswordlessClient manually: Ensure the context is set to the current `Activity`.

```kotlin
/** Context needs to be set according to current activity
 * If there are different activity handling register and signin,
 * then call this on every activity
*/
_passwordless.setContext(this)
```

Set the coroutine scope, passing lifecycleScope of the current fragment, only necessary if you again do not use Dagger Hilt.

```kotlin
_passwordless.setCoroutineScope(lifecycleScope)
```

### Registration

1. **Call Your Backend with User Details**:Make a call to your backend with user details (e.g., username, alias) and retrieve the registration token.
2. **Call Passwordless Register Function**

```kotlin
_passwordless.register(
    token =responseToken,
    nickname = nickname
) { success, exception, result ->
        if (success) {
            Toast.makeText(context, result.toString(), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(
            context,
            "Exception: " + getPasskeyFailureMessage(exception as Exception),
        Toast.LENGTH_SHORT
        ).show()
    }
}
```

### Logging in

1. **Take Alias as Input**: Gather the alias as input from the user.
2. **Call Passwordless Login**: Initiate the login process with the alias and response callback.

```kotlin
_passwordless.login(alias) { success, exception, result ->
    if (success) {
        lifecycleScope.launch {
             val clientDataResponse =
                httpClient.login(UserLoginRequest(result?.token!!))
            if (clientDataResponse.isSuccessful) {
                val data = clientDataResponse.body()
                showText(data.toString())
            }
        }
    } else {
        showException(exception)
    }
}
```

## References

- [Get Started - Passwordless.dev](../get-started.md/)
- [Integration with your backend - Passwordless.dev](../backend/index.md)
- [Client Auth - Google](https://developers.google.com/android/guides/client-auth)
- [Associate apps & sites - Google](https://developers.google.com/identity/smartlock-passwords/android/associate-apps-and-sites)
- [Passkeys - Google](https://developer.android.com/training/sign-in/passkeys)
