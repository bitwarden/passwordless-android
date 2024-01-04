package dev.passwordless.android.rest

import androidx.credentials.CreatePublicKeyCredentialRequest
import com.google.gson.Gson
import com.google.gson.JsonObject
import dev.passwordless.android.rest.contracts.register.RegisterBeginRequest
import dev.passwordless.android.rest.contracts.register.RegisterBeginResponse
import dev.passwordless.android.rest.serializers.Base64UrlDeserializer
import dev.passwordless.android.rest.serializers.CreatePublicKeyCredentialRequestDeserializer
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PasswordlessHttpClientTests {
    private lateinit var mockWebServer: MockWebServer
    private lateinit var apiService: PasswordlessHttpClient
    private lateinit var gson: Gson
    private lateinit var mockDeserializer: CreatePublicKeyCredentialRequestDeserializer
    private lateinit var mockCreatePublicKeyCredentialRequest: CreatePublicKeyCredentialRequest

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()

        // Crating mock as CreatePublicKeyCredentialRequest doesn't work in test context
        mockDeserializer = mock(CreatePublicKeyCredentialRequestDeserializer::class.java)
        mockCreatePublicKeyCredentialRequest = mock(CreatePublicKeyCredentialRequest::class.java)
        gson = Gson().newBuilder()
            .registerTypeAdapter(ByteArray::class.java, Base64UrlDeserializer())
            .registerTypeAdapter(CreatePublicKeyCredentialRequest::class.java, mockDeserializer)
            .create()
        `when`(mockDeserializer.deserialize(any(), any(), any())).thenReturn(
            mockCreatePublicKeyCredentialRequest
        )
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
            "https://adminconsole.passwordless.dev"
        )

        val jsonString = "{\n" +
                "  \"data\": {\n" +
                "    \"rp\": {\n" +
                "      \"id\": \"990b-163-53-252-8.ngrok-free.app\",\n" +
                "      \"name\": \"990b-163-53-252-8.ngrok-free.app\"\n" +
                "    },\n" +
                "    \"user\": {\n" +
                "      \"name\": \"hdhdbd\",\n" +
                "      \"id\": \"ZGMyOGFiYmEtZGVlNC00YThlLWE1Y2YtMjZjNTZiODQ4OWU2\",\n" +
                "      \"displayName\": \"hdhdbd\"\n" +
                "    },\n" +
                "    \"challenge\": \"HWSPmp_eoc3w5jgABTwhpw\",\n" +
                "    \"pubKeyCredParams\": [\n" +
                "      {\n" +
                "        \"type\": \"public-key\",\n" +
                "        \"alg\": -8\n" +
                "      },\n" +
                "      {\n" +
                "        \"type\": \"public-key\",\n" +
                "        \"alg\": -7\n" +
                "      },\n" +
                "      {\n" +
                "        \"type\": \"public-key\",\n" +
                "        \"alg\": -257\n" +
                "      },\n" +
                "      {\n" +
                "        \"type\": \"public-key\",\n" +
                "        \"alg\": -37\n" +
                "      },\n" +
                "      {\n" +
                "        \"type\": \"public-key\",\n" +
                "        \"alg\": -35\n" +
                "      },\n" +
                "      {\n" +
                "        \"type\": \"public-key\",\n" +
                "        \"alg\": -258\n" +
                "      },\n" +
                "      {\n" +
                "        \"type\": \"public-key\",\n" +
                "        \"alg\": -38\n" +
                "      },\n" +
                "      {\n" +
                "        \"type\": \"public-key\",\n" +
                "        \"alg\": -36\n" +
                "      },\n" +
                "      {\n" +
                "        \"type\": \"public-key\",\n" +
                "        \"alg\": -259\n" +
                "      },\n" +
                "      {\n" +
                "        \"type\": \"public-key\",\n" +
                "        \"alg\": -39\n" +
                "      }\n" +
                "    ],\n" +
                "    \"timeout\": 60000,\n" +
                "    \"attestation\": \"none\",\n" +
                "    \"authenticatorSelection\": {\n" +
                "      \"residentKey\": \"required\",\n" +
                "      \"requireResidentKey\": true,\n" +
                "      \"userVerification\": \"preferred\"\n" +
                "    },\n" +
                "    \"excludeCredentials\": [],\n" +
                "    \"extensions\": {\n" +
                "      \"credProps\": true\n" +
                "    },\n" +
                "    \"status\": \"ok\",\n" +
                "    \"errorMessage\": \"\"\n" +
                "  },\n" +
                "  \"session\": \"session_k8QgN2JiyYtaoaDU9-Z75sZzTm-Kp42D0H4AffaF1mQTBrnFApqDp09wdGlvbnOLplN0YXR1c6Jva6xFcnJvck1lc3NhZ2WgolJwg6JJZNkgOTkwYi0xNjMtNTMtMjUyLTgubmdyb2stZnJlZS5hcHCkTmFtZdkgOTkwYi0xNjMtNTMtMjUyLTgubmdyb2stZnJlZS5hcHCkSWNvbsCkVXNlcoOkTmFtZaZoZGhkYmSiSWTEJGRjMjhhYmJhLWRlZTQtNGE4ZS1hNWNmLTI2YzU2Yjg0ODllNqtEaXNwbGF5TmFtZaZoZGhkYmSpQ2hhbGxlbmdlxBAdZI-an96hzfDmOAAFPCGnsFB1YktleUNyZWRQYXJhbXOagqRUeXBlAKNBbGf4gqRUeXBlAKNBbGf5gqRUeXBlAKNBbGfR_v-CpFR5cGUAo0FsZ9DbgqRUeXBlAKNBbGfQ3YKkVHlwZQCjQWxn0f7-gqRUeXBlAKNBbGfQ2oKkVHlwZQCjQWxn0NyCpFR5cGUAo0FsZ9H-_YKkVHlwZQCjQWxn0NmnVGltZW91dM3qYKtBdHRlc3RhdGlvbgC2QXV0aGVudGljYXRvclNlbGVjdGlvboS3QXV0aGVudGljYXRvckF0dGFjaG1lbnTAq1Jlc2lkZW50S2V5ALJSZXF1aXJlUmVzaWRlbnRLZXnDsFVzZXJWZXJpZmljYXRpb24BskV4Y2x1ZGVDcmVkZW50aWFsc5CqRXh0ZW5zaW9uc4inRXhhbXBsZcClQXBwSUTAtkF1dGhlbnRpY2F0b3JTZWxlY3Rpb27AqkV4dGVuc2lvbnPAtlVzZXJWZXJpZmljYXRpb25NZXRob2TArERldmljZVB1YktlecCpQ3JlZFByb3Bzw6NQUkbAp0FsaWFzZXORpWZpZnVqrEFsaWFzSGFzaGluZ8POAnDXnQ\"\n" +
                "}"
        val mockedResponse = MockResponse()
            .setResponseCode(200)
            .setBody(jsonString)
        val mockRequestJson =
            gson.fromJson(jsonString, JsonObject::class.java).getAsJsonObject("data")
        mockWebServer.enqueue(mockedResponse)
        `when`(mockCreatePublicKeyCredentialRequest.requestJson).thenReturn(mockRequestJson.toString())

        // act
        val actualResponse = apiService.registerBegin(inputModel)
        mockWebServer.takeRequest()
        assert(actualResponse.isSuccessful)
        val actual: RegisterBeginResponse = actualResponse.body()!!
        assertEquals(
            "session_k8QgN2JiyYtaoaDU9-Z75sZzTm-Kp42D0H4AffaF1mQTBrnFApqDp09wdGlvbnOLplN0YXR1c6Jva6xFcnJvck1lc3NhZ2WgolJwg6JJZNkgOTkwYi0xNjMtNTMtMjUyLTgubmdyb2stZnJlZS5hcHCkTmFtZdkgOTkwYi0xNjMtNTMtMjUyLTgubmdyb2stZnJlZS5hcHCkSWNvbsCkVXNlcoOkTmFtZaZoZGhkYmSiSWTEJGRjMjhhYmJhLWRlZTQtNGE4ZS1hNWNmLTI2YzU2Yjg0ODllNqtEaXNwbGF5TmFtZaZoZGhkYmSpQ2hhbGxlbmdlxBAdZI-an96hzfDmOAAFPCGnsFB1YktleUNyZWRQYXJhbXOagqRUeXBlAKNBbGf4gqRUeXBlAKNBbGf5gqRUeXBlAKNBbGfR_v-CpFR5cGUAo0FsZ9DbgqRUeXBlAKNBbGfQ3YKkVHlwZQCjQWxn0f7-gqRUeXBlAKNBbGfQ2oKkVHlwZQCjQWxn0NyCpFR5cGUAo0FsZ9H-_YKkVHlwZQCjQWxn0NmnVGltZW91dM3qYKtBdHRlc3RhdGlvbgC2QXV0aGVudGljYXRvclNlbGVjdGlvboS3QXV0aGVudGljYXRvckF0dGFjaG1lbnTAq1Jlc2lkZW50S2V5ALJSZXF1aXJlUmVzaWRlbnRLZXnDsFVzZXJWZXJpZmljYXRpb24BskV4Y2x1ZGVDcmVkZW50aWFsc5CqRXh0ZW5zaW9uc4inRXhhbXBsZcClQXBwSUTAtkF1dGhlbnRpY2F0b3JTZWxlY3Rpb27AqkV4dGVuc2lvbnPAtlVzZXJWZXJpZmljYXRpb25NZXRob2TArERldmljZVB1YktlecCpQ3JlZFByb3Bzw6NQUkbAp0FsaWFzZXORpWZpZnVqrEFsaWFzSGFzaGluZ8POAnDXnQ",
            actual.session
        )

        val requestJson = gson.fromJson(actual.data.requestJson, JsonObject::class.java)
        assertEquals("HWSPmp_eoc3w5jgABTwhpw", requestJson.get("challenge").asString)
        assertEquals("60000", requestJson.get("timeout").asString)
        assertEquals("ok", requestJson.get("status").asString)
        assertEquals("", requestJson.get("errorMessage").asString)
    }
}
