package dev.passwordless.sampleapp
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
        const val API_KEY = "jonasapp1:public:a13666586ea448958c38cdec76eaaace"
        const val RP_ID = "pwdemo.lesspassword.dev"
        const val YOUR_BACKEND_URL = "https://pwdemo.lesspassword.dev"
        const val ORIGIN = "android:apk-key-hash:POIplOLeHuvl-XAQckH0DwY4Yb1ydnnKcmhn-jibZbk"
        const val API_URL = "https://v4.passwordless.dev"
    }
}
