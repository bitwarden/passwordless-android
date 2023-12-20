package com.example.myapplication.services.yourbackend.config
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
        const val API_KEY = "yourapp:public:ab2e4350d43946f7b4c93d98fa2c765e"
        const val RP_ID = "example.com"
        const val YOUR_BACKEND_URL = "https://example.com"
        const val ORIGIN = "android:apk-key-hash:NX7853gQH6KKGF4iT7WmpEtBDw7njd75WuaAFKzyW44"
        const val API_URL = "https://v4.passwordless.dev"
    }
}
