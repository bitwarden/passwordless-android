package dev.passwordless.sampleapp.config
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
        const val API_KEY = "pwdemo:public:5aec1f24f65343239bf4e1c9a852e871"
        const val RP_ID = "demo.lesspassword.dev"
        const val YOUR_BACKEND_URL = "https://demo.lesspassword.dev"
        const val ORIGIN = "android:apk-key-hash:oSQ_L7vpI6fdhEtKK6QKxy7A1o2GkJTK569M3toUIWU"
        const val API_URL = "https://v4.passwordless.dev"
    }
}
