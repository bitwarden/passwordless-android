package dev.passwordless.android.rest.interceptors

import dev.passwordless.android.rest.exceptions.PasswordlessApiException
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.io.IOException

class InterceptorTests {
    private lateinit var _server: MockWebServer
    private lateinit var _interceptor: ProblemDetailsInterceptor

    @Before
    @Throws(IOException::class)
    fun setup() {
        _server = MockWebServer()
        _server.start()
        _interceptor = ProblemDetailsInterceptor()
    }

    @After
    @Throws(IOException::class)
    fun shutdown() {
        _server.shutdown()
    }

    @Test
    @Throws(InterruptedException::class)
    fun intercept_returns_response_when_successful() {
        _server.enqueue(MockResponse().setResponseCode(200))
        val client: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(_interceptor)
            .build()
        val request = Request.Builder()
            .url(_server.url("/"))
            .build()
        try {
            client.newCall(request).execute()
            // If successful, the interceptor should not throw an exception
        } catch (e: IOException) {
            Assert.fail("Interceptor should not throw an exception for successful response")
        }
    }

    @Test
    @Throws(PasswordlessApiException::class)
    fun intercept_throws_PasswordlessApiException_when_ProblemJsonContentTypeHeaderIsPresent() {
        val expectedJson = """
            {
                "type": "https://docs.passwordless.dev/guide/errors.html#ApiKey",
                "title": "A valid 'ApiKey' header is required.",
                "status": 401,
                "detail": "A valid 'ApiKey' header is required."
            }
        """
        _server.enqueue(MockResponse()
            .setResponseCode(401)
            .setHeader("Content-Type", "application/problem+json")
            .setBody(expectedJson))

        val client: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(_interceptor)
            .build()
        val request = Request.Builder()
            .url(_server.url("/"))
            .build()
        try {
            client.newCall(request).execute()
            Assert.fail("Interceptor should throw an exception for unsuccessful response")
        } catch (e: PasswordlessApiException) {
            Assert.assertNotNull(e.details)
            Assert.assertEquals(401, e.details.status)
            Assert.assertEquals("A valid 'ApiKey' header is required.", e.details.detail)
            Assert.assertEquals("A valid 'ApiKey' header is required.", e.details.title)
            Assert.assertEquals("https://docs.passwordless.dev/guide/errors.html#ApiKey", e.details.type)
        } catch (e: IOException) {
            Assert.fail("Unexpected exception.")
        }
    }
}
