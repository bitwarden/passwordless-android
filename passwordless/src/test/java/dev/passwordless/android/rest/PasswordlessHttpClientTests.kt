package dev.passwordless.android.rest

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dev.passwordless.android.rest.contracts.*
import dev.passwordless.android.rest.converters.Base64UrlConverter
import dev.passwordless.android.rest.serializers.Base64UrlDeserializer
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PasswordlessHttpClientTests {
    private lateinit var mockWebServer: MockWebServer
    private lateinit var apiService: PasswordlessHttpClient

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()

        val gson: Gson = GsonBuilder()
            .registerTypeAdapter(ByteArray::class.java, Base64UrlDeserializer())
            .create()

        apiService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(PasswordlessHttpClient::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun registerBegin_returns_expectedResult() = runBlocking {
        // arrange
        val inputModel = RegisterBeginRequest(
            "register_token",
            "adminconsole.passwordless.dev",
            "https://adminconsole.passwordless.dev")

        val mockedResponse = MockResponse()
            .setResponseCode(200)
            .setBody("{\n" +
                    "    \"data\": {\n" +
                    "        \"challenge\": \"GuRnSHGx9QyTLRct86QJtA\",\n" +
                    "        \"timeout\": 60000,\n" +
                    "        \"rpId\": \"adminconsole01.lesspassword.dev\",\n" +
                    "        \"allowCredentials\": [],\n" +
                    "        \"userVerification\": \"preferred\",\n" +
                    "        \"status\": \"ok\",\n" +
                    "        \"errorMessage\": \"\"\n" +
                    "    },\n" +
                    "    \"session\": \"session_k8QgX8WVditXejKDs4Pyzs6fJXyyW4PeuKVqFRCViPXKPIHElYimU3RhdHVzom9rrEVycm9yTWVzc2FnZaCpQ2hhbGxlbmdlxBAa5GdIcbH1DJMtFy3zpAm0p1RpbWVvdXTN6mCkUnBJZL9hZG1pbmNvbnNvbGUwMS5sZXNzcGFzc3dvcmQuZGV2sEFsbG93Q3JlZGVudGlhbHOQsFVzZXJWZXJpZmljYXRpb24BqkV4dGVuc2lvbnPAziGmeas\"\n" +
                    "}")

        mockWebServer.enqueue(mockedResponse)

        // act
        val actualResponse = apiService.registerBegin(inputModel)

        mockWebServer.takeRequest()
        assert(actualResponse.isSuccessful)
        val actual: RegisterBeginResponse = actualResponse.body()!!
        assertEquals("session_k8QgX8WVditXejKDs4Pyzs6fJXyyW4PeuKVqFRCViPXKPIHElYimU3RhdHVzom9rrEVycm9yTWVzc2FnZaCpQ2hhbGxlbmdlxBAa5GdIcbH1DJMtFy3zpAm0p1RpbWVvdXTN6mCkUnBJZL9hZG1pbmNvbnNvbGUwMS5sZXNzcGFzc3dvcmQuZGV2sEFsbG93Q3JlZGVudGlhbHOQsFVzZXJWZXJpZmljYXRpb24BqkV4dGVuc2lvbnPAziGmeas", actual.session)
        assertEquals("GuRnSHGx9QyTLRct86QJtA", Base64UrlConverter.convert(actual.data.challenge))
        assertEquals(60000L, actual.data.timeout)
        assertEquals("ok", actual.data.status)
        assertEquals("", actual.data.errorMessage)
    }
}
