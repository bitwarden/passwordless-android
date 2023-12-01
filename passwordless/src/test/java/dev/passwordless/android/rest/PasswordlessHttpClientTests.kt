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
import java.util.*


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
        val inputModel = RegisterBeginRequest("register_token")
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

        val request = mockWebServer.takeRequest()
        assert(actualResponse.isSuccessful)
        val actual: RegisterBeginResponse = actualResponse.body()!!
        assertEquals("session_k8QgX8WVditXejKDs4Pyzs6fJXyyW4PeuKVqFRCViPXKPIHElYimU3RhdHVzom9rrEVycm9yTWVzc2FnZaCpQ2hhbGxlbmdlxBAa5GdIcbH1DJMtFy3zpAm0p1RpbWVvdXTN6mCkUnBJZL9hZG1pbmNvbnNvbGUwMS5sZXNzcGFzc3dvcmQuZGV2sEFsbG93Q3JlZGVudGlhbHOQsFVzZXJWZXJpZmljYXRpb24BqkV4dGVuc2lvbnPAziGmeas", actual.session)
        assertEquals("GuRnSHGx9QyTLRct86QJtA", Base64UrlConverter.convert(actual.data.challenge))
        assertEquals(60000L, actual.data.timeout)
        assertEquals("preferred", actual.data.userVerification)
        assertEquals("ok", actual.data.status)
        assertEquals("", actual.data.errorMessage)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun registerComplete_returns_expectedResult() = runBlocking {
        // arrange
        val inputModel = RegisterCompleteRequest(
            session = "session_token",
            AuthenticatorAttestationRawResponse(
                id = Base64UrlConverter.convert("3a_f0ZxJepCUAMeaU8xXUUWiTCfvwOkMv-UOz5OQp4M"),
                rawId = Base64UrlConverter.convert("3a_f0ZxJepCUAMeaU8xXUUWiTCfvwOkMv-UOz5OQp4M"),
                type = "public-key",
                response = ResponseData(
                    attestationObject = Base64UrlConverter.convert("o2NmbXRkbm9uZWdhdHRTdG10oGhhdXRoRGF0YVikUpfHoBfwKwWPVOaB_kMIan16wUyi5nRVDX-0W96AJkZFAAAAAK3OAAI1vMYKZIsLJfHwVQMAIN2v39GcSXqQlADHmlPMV1FFokwn78DpDL_lDs-TkKeDpQECAyYgASFYIPqLAH7mRCTdK0T-HEnIqumDtYK1zU01Wwr2v-SaU3aNIlggKFqOipbIQeYZKs3fZ2onmBJzksFOyEi7NZ6c9XNK50s"),
                    clientDataJSON = Base64UrlConverter.convert("eyJ0eXBlIjoid2ViYXV0aG4uY3JlYXRlIiwiY2hhbGxlbmdlIjoiVlE5Ty1lZkpwYS1mbElQTG5YaGV6ZyIsIm9yaWdpbiI6Imh0dHBzOi8vYWRtaW5jb25zb2xlMDEubGVzc3Bhc3N3b3JkLmRldiIsImNyb3NzT3JpZ2luIjpmYWxzZSwib3RoZXJfa2V5c19jYW5fYmVfYWRkZWRfaGVyZSI6ImRvIG5vdCBjb21wYXJlIGNsaWVudERhdGFKU09OIGFnYWluc3QgYSB0ZW1wbGF0ZS4gU2VlIGh0dHBzOi8vZ29vLmdsL3lhYlBleCJ9")
                ),
                extensions = AuthenticationExtensionsClientOutputs(
                    credProps = CredentialPropertiesOutput(rk = true),
                )
            ),
            "",
            "https://adminconsole.passwordless.dev",
            "adminconsole.passwordless.dev")

        val mockedResponse = MockResponse()
            .setResponseCode(200)
            .setBody("{\n" +
                    "    \"token\": \"verify_token\"\n" +
                    "}")

        mockWebServer.enqueue(mockedResponse)

        // act
        val actualResponse = apiService.registerComplete(inputModel)

        val request = mockWebServer.takeRequest()
        assert(actualResponse.isSuccessful)
        val actual: RegisterCompleteResponse = actualResponse.body()!!
        assertEquals("verify_token", actual.token)
    }
}
