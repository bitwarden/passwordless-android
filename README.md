## Get started

Passwordless.dev consists of three key parts:

* An open-source client side library, used by your frontend to make requests to the end-user's browser WebAuthn API and requests to the passwordless.dev APIs.
    * [JavaScript Client](https://github.com/bitwarden/passwordless-client-js): JavaScript
    * [Android Client](https://github.com/bitwarden/passwordless-android): Java, Kotlin
* A public RESTful API used to complete FIDO2 WebAuthn cryptographic exchanges with the browser.
    * [Register here](https://admin.passwordless.dev/Account/Login)
* a private RESTful API used to initiate key registrations, verify signins, and retrieve keys for end-users. This is typically your backend.

## Requirements
* Android 9+ (API 28)
## Setting up Passwordless-android
1. Generate SHA256 for your app.
#### Option 1:

#### Windows 
```cmd
keytool -list -v -keystore "%USERPROFILE%\.android\debug.keystore" -alias androiddebugkey -storepass android -keypass android
```
#### MAC/Linux
```bash
keytool -list -v -keystore ~/.android/debug.keystore -alias androiddebugkey -storepass android -keypass android
```
#### Option 2:
Go to the root directory of the project from the terminal and run the below command
```bash
./gradlew signingReport
```
2. Put this SHA256 along with your root android package name in your backend to generate assetlink.json for your app at "https://yourexample.com/.well-known/assetlinks.json"
If you are using [example-nodejs-backend](https://github.com/passwordless/passwordless-nodejs-example). then just put these 2 variables in .env file.

4. Generate APK key hash, this hash is required in the origin field to authenticate the app passwordless.
#### MAC/Linux
```bash
keytool -list -v -keystore ~/.android/debug.keystore | grep "SHA256: " | cut -d " " -f 3 | xxd -r -p | openssl base64 | sed 's/=//g'
```
#### Windows 
Run this on PowerShell
```powershell
# Run keytool command and extract SHA256 hash
$keytoolOutput = keytool -list -v -keystore $HOME\.android\debug.keystore
$sha256Hash = ($keytoolOutput | Select-String "SHA256: ").ToString().Split(" ")[2]

# Remove any non-hex characters from the hash
$hexHash = $sha256Hash -replace "[^0-9A-Fa-f]"

# Convert the hexadecimal string to a byte array
$byteArray = [byte[]]@()
for ($i = 0; $i -lt $hexHash.Length; $i += 2) {
$byteArray += [byte]([Convert]::ToUInt32($hexHash.Substring($i, 2), 16))
}

# Convert the byte array to a base64 string
$base64String = [Convert]::ToBase64String($byteArray)

Write-Output $base64String
```
 4. Get your public key by registering at https://admin.passwordless.dev/Account/Login
 5. Now, update yourbackend/config/DemoPasswordlessOptions.kt and xml/assetlinks.xml
 6. Run the app!

## Run the Example App
1. **Setup `PasswordlessOptions` Class**: Begin by configuring the PasswordlessOptions class, as demonstrated in the [example](app/src/main/java/dev/passwordless/sampleapp/services/yourbackend/config/DemoPasswordlessOptions.kt). Fill in the required parameters:
``` kotlin
/**
 * @property API_URL The Passwordless.dev server url.
 * @property API_KEY Your public API key.
 * @property RP_ID This stands for “relying party”; it can be considered as describing the organization responsible for registering and authenticating the user.
 * Set this as base url for your backend. So, https://<Relying Party ID>/.well-known/assetlinks.json is accessible
 * @property ORIGIN This is your generated key for your app, refer readme on how to generate this.
 * String Format: "android:apk-key-hash:<Hash value>" , Example "android:apk-key-hash:NX7853gQH6KKGF4iT7WmpEtBDw7njd75WuaAFKzyW44"
 * @property YOUR_BACKEND_URL This is where your backend is hosted.
 */
class DemoPasswordlessOptions {
    companion object {
        const val API_KEY = "personal:public:fef3b07e9022454cb72377b96d1ac329"
        const val RP_ID = "990b-163-53-252-8.ngrok-free.app"
        const val YOUR_BACKEND_URL = "https://990b-163-53-252-8.ngrok-free.app"
        const val ORIGIN = "android:apk-key-hash:NX7853gQH6KKGF4iT7WmpEtBDw7njd75WuaAFKzyW44"
        const val API_URL = "https://v4.passwordless.dev"
    }
}
```
2. Update Assetlinks File: Modify the assetlinks [file here](app/src/main/res/xml/assetlinks.xml)
``` xml
<resources>
    <string name="assetlinks">https://yourexample.com/.well-known/assetlinks.json</string>
</resources>
```
## Using SDK
1. **Declare `PasswordlessClient`**: Establish a singleton `PasswordlessClient` object using the `PasswordlessOptions` class. Utilize the values from `DemoPasswordlessOptions`:
``` kotlin
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
```
2. **Set the Context of PasswordlessClient**:Ensure the context is set to the current activity. Note that this must be an Activity context of the current activity.

[MainActivity.kt](app/src/main/java/dev/passwordless/sampleapp/MainActivity.kt)
``` kotlin
        /** Context needs to be set according to current activity
         * If there are different activity handling register and signin,
         * then call this on every activity
         */

        _passwordless.setContext(this)
```
3. **Set Coroutine Scope**: Set the coroutine scope, passing lifecycleScope of the current fragment.
   
[RegisterFragment.kt](app/src/main/java/dev/passwordless/sampleapp/RegisterFragment.kt) , [LoginFragment.kt](app/src/main/java/dev/passwordless/sampleapp/LoginFragment.kt)
``` kotlin
        //Scope needs to be updated according to current class
        _passwordless
            .setCoroutineScope(lifecycleScope)
```
### Register
1. **Call Your Backend with User Details**:Make a call to your backend with user details (e.g., username, alias) and retrieve the registration token.
2. **Call Passwordless Register Function**

[RegisterFragment.kt](app/src/main/java/dev/passwordless/sampleapp/RegisterFragment.kt)
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

### Signin
1. **Take Alias as Input**: Gather the alias as input from the user.
2. **Call Passwordless Login**: Initiate the login process with the alias and response callback.

[LoginFragment.kt](app/src/main/java/dev/passwordless/sampleapp/LoginFragment.kt)
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
## Signup flow 
![signup flow f](https://github.com/bitwarden/passwordless-android/assets/56815364/aea379de-aacb-4619-a650-41bf67f15d7d)

## Signin flow
![signin](https://github.com/bitwarden/passwordless-android/assets/56815364/2fa48f99-e412-4f1d-800f-d9109ba9ff4f)

## Help
To talk to the passwordless team, send us an email at support@passwordless.dev


## Bitwarden

You can find Bitwarden's other code repositories at https://github.com/bitwarden and more information on https://bitwarden.com/.
