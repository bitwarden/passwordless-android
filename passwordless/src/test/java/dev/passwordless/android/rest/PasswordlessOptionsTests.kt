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
        val apiUrl = "https://v4.passwordless.dev"

        // act
        sut = PasswordlessOptions(apiKey, rpId, apiUrl)

        // assert
        assertEquals(apiKey, sut.apiKey)
        assertEquals(rpId, sut.rpId)
        assertEquals(apiUrl, sut.apiUrl)
    }

    @Test
    fun constructor_hasCorrectApiUrlDefaultValue() {
        // arrange
        val apiKey = "jonasandroid:public:ab2e4350d43946f7b4c93d98fa2c765e"
        val rpId = "example.com"
        val apiUrl = "https://v4.passwordless.dev"

        // act
        sut = PasswordlessOptions(apiKey, rpId)

        // assert
        assertEquals(apiUrl, sut.apiUrl)
    }

    @Test
    fun apiKey_throws_illegalArgumentException_whenBlank() {
        // arrange
        val apiKey = "jonasandroid:public:ab2e4350d43946f7b4c93d98fa2c765e"
        val rpId = ""
        val apiUrl = "https://v4.passwordless.dev"

        // act
        val expected = "rpId must not be blank"
        assertThrows(expected, IllegalArgumentException::class.java) {
            sut = PasswordlessOptions(apiKey, rpId, apiUrl)
        }
    }

    @Test
    fun apiKey_throws_illegalArgumentException_whenIncorrectFormat() {
        // arrange
        val apiKey = "badkey"
        val rpId = "yourexample.com"
        val apiUrl = "https://v4.passwordless.dev"

        // act
        val expected = "apiKey must be a valid API key '<appname>:public:<uuid-without-dashes>'"
        assertThrows(expected, IllegalArgumentException::class.java) {
            sut = PasswordlessOptions(apiKey, rpId, apiUrl)
        }
    }

    @Test
    fun rpId_throws_illegalArgumentException_whenBlank() {
        // arrange
        val apiKey = ""
        val rpId = "example.com"
        val apiUrl = "https://v4.passwordless.dev"

        // act
        assertThrows("apiKey must not be blank", IllegalArgumentException::class.java) {
            sut = PasswordlessOptions(apiKey, rpId, apiUrl)
        }
    }
}
