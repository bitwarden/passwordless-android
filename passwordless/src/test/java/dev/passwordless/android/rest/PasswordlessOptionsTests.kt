package dev.passwordless.android.rest

import junit.framework.TestCase.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class PasswordlessOptionsTests {
    private lateinit var sut: PasswordlessOptions

    @Test
    fun constructor_instantiates_object_withValidParameters() {
        // arrange
        val apiKey = "jonasandroid:public:ab2e4350d43946f7b4c93d98fa2c765e"
        val rpId = "example.com"
        val origin = "android:apk-key-hash:POIplOLeHuvl-XAQckH0DwY4Yb1ydnnKcmhn-jibZbk"
        val apiUrl = "https://v4.passwordless.dev"

        // act
        sut = PasswordlessOptions(apiKey, rpId, origin, apiUrl)

        // assert
        assertEquals(apiKey, sut.apiKey)
        assertEquals(rpId, sut.rpId)
        assertEquals(origin, sut.origin)
        assertEquals(apiUrl, sut.apiUrl)
    }

    @Test
    fun constructor_hasCorrectApiUrlDefaultValue() {
        // arrange
        val apiKey = "jonasandroid:public:ab2e4350d43946f7b4c93d98fa2c765e"
        val rpId = "example.com"
        val origin = "android:apk-key-hash:POIplOLeHuvl-XAQckH0DwY4Yb1ydnnKcmhn-jibZbk"
        val apiUrl = "https://v4.passwordless.dev"

        // act
        sut = PasswordlessOptions(apiKey, rpId, origin)

        // assert
        assertEquals(apiUrl, sut.apiUrl)
    }

    @Test
    fun apiKey_throws_illegalArgumentException_whenBlank() {
        // arrange
        val apiKey = "jonasandroid:public:ab2e4350d43946f7b4c93d98fa2c765e"
        val rpId = ""
        val origin = "android:apk-key-hash:POIplOLeHuvl-XAQckH0DwY4Yb1ydnnKcmhn-jibZbk"
        val apiUrl = "https://v4.passwordless.dev"

        // act
        assertThrows("rpId must not be blank", IllegalArgumentException::class.java) {
            sut = PasswordlessOptions(apiKey, rpId, origin, apiUrl)
        }
    }

    @Test
    fun apiKey_throws_illegalArgumentException_whenIncorrectFormat() {
        // arrange
        val apiKey = "badkey"
        val rpId = "yourexample.com"
        val origin = "android:apk-key-hash:POIplOLeHuvl-XAQckH0DwY4Yb1ydnnKcmhn-jibZbk"
        val apiUrl = "https://v4.passwordless.dev"

        // act
        assertThrows("apiKey must be a valid API key '<appname>:public:<uuid-without-dashes>'", IllegalArgumentException::class.java) {
            sut = PasswordlessOptions(apiKey, rpId, origin, apiUrl)
        }
    }

    @Test
    fun rpId_throws_illegalArgumentException_whenBlank() {
        // arrange
        val apiKey = ""
        val rpId = "example.com"
        val origin = "android:apk-key-hash:POIplOLeHuvl-XAQckH0DwY4Yb1ydnnKcmhn-jibZbk"
        val apiUrl = "https://v4.passwordless.dev"

        // act
        assertThrows("apiKey must not be blank", IllegalArgumentException::class.java) {
            sut = PasswordlessOptions(apiKey, rpId, origin, apiUrl)
        }
    }

    @Test
    fun origin_throws_illegalArgumentException_whenIncorrectBase64Url() {
        // arrange
        val apiKey = "jonasandroid:public:ab2e4350d43946f7b4c93d98fa2c765e"
        val rpId = "example.com"
        val origin = "android:apk-key-hash:POIplOLeHuvl+XAQckH0DwY4Yb1ydnnKcmhn+jibZbk"
        val apiUrl = "https://v4.passwordless.dev"

        // act
        assertThrows("origin must be a valid URL", IllegalArgumentException::class.java) {
            sut = PasswordlessOptions(apiKey, rpId, origin, apiUrl)
        }
    }

    @Test
    fun origin_throws_illegalArgumentException_whenNotFacetId() {
        // arrange
        val apiKey = "jonasandroid:public:ab2e4350d43946f7b4c93d98fa2c765e"
        val rpId = "example.com"
        val origin = "https://yourbackend.com"
        val apiUrl = "https://v4.passwordless.dev"

        // act
        assertThrows("origin must be a facet id 'android:apk-key-hash:base64url'", IllegalArgumentException::class.java) {
            sut = PasswordlessOptions(apiKey, rpId, origin, apiUrl)
        }
    }
}
