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

## Signup flow 
![signup flow](https://github.com/shubhamji88/passwordless-android/assets/56815364/f7ce294f-68e6-4b61-844b-b3db7f9f17d5)

## Help
To talk to the passwordless team, send us an email at support@passwordless.dev


## Bitwarden

You can find Bitwarden's other code repositories at https://github.com/bitwarden and more information on https://bitwarden.com/.
