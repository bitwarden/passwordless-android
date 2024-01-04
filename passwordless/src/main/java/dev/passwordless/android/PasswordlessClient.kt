package dev.passwordless.android

import android.content.Context
import android.util.Log
import androidx.credentials.CreatePublicKeyCredentialRequest
import androidx.credentials.CreatePublicKeyCredentialResponse
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetPublicKeyCredentialOption
import androidx.credentials.PublicKeyCredential
import dev.passwordless.android.rest.PasswordlessHttpClient
import dev.passwordless.android.rest.PasswordlessHttpClientFactory
import dev.passwordless.android.rest.PasswordlessOptions
import dev.passwordless.android.rest.contracts.login.LoginBeginRequest
import dev.passwordless.android.rest.contracts.login.LoginBeginResponse
import dev.passwordless.android.rest.contracts.login.LoginCompleteRequest
import dev.passwordless.android.rest.contracts.login.LoginCompleteResponse
import dev.passwordless.android.rest.contracts.register.RegisterBeginRequest
import dev.passwordless.android.rest.contracts.register.RegisterBeginResponse
import dev.passwordless.android.rest.contracts.register.RegisterCompleteRequest
import dev.passwordless.android.rest.contracts.register.RegisterCompleteResponse
import dev.passwordless.android.rest.converters.PublicKeyCredentialConverter
import dev.passwordless.android.rest.exceptions.PasswordlessApiException
import dev.passwordless.android.rest.exceptions.ProblemDetails
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

/**
 * Manages the communication with the Credential Manager API and performs registration operations for the Passwordless authentication flow.
 *
 * @param options The configuration options for Passwordless authentication.
 */
class PasswordlessClient(
    private val _options: PasswordlessOptions
) {
    private val _httpClient: PasswordlessHttpClient = PasswordlessHttpClientFactory.create(_options)

    private lateinit var _coroutineScope: CoroutineScope
    private lateinit var credentialManager: CredentialManager
    private lateinit var _context: Context
    fun setCoroutineScope(coroutineScope: CoroutineScope): PasswordlessClient =
        apply { _coroutineScope = coroutineScope }

    fun setContext(context: Context): PasswordlessClient =
        apply {
            _context = context
            credentialManager = CredentialManager.create(_context)
        }

    private inline fun <reified T> Response<T>.throwIfNetworkRequestFailed(): Response<T> {
        if (!isSuccessful || body() == null) {
            val problemDetails = ProblemDetails("", message(), code(), "")
            throw PasswordlessApiException(problemDetails)
        }
        return this
    }

    /**
     * Initiates the login process for Passwordless authentication.
     *
     * @param alias The user alias for authentication.
     * @param onLoginResult Callback function to handle the login result.
     *                     It provides a boolean indicating success, an optional exception in case of failure,
     *                     and the response containing information about the completed login if successful.
     */

    fun login(
        alias: String,
        onLoginResult: (Boolean, Exception?, LoginCompleteResponse?) -> Unit
    ) = _coroutineScope.launch(Dispatchers.IO) {
        try {
            val beginInputModel = LoginBeginRequest(
                alias = alias,
                rpId = _options.rpId,
                origin = _options.origin
            )
            val beginResponse = _httpClient
                .loginBegin(beginInputModel)
                .throwIfNetworkRequestFailed()

            val beginResponseData: LoginBeginResponse = beginResponse.body()!!

            val credentialResponse = credentialManager.getCredential(
                _context,
                GetCredentialRequest(
                    listOf(
                        beginResponseData.data
                    )
                )
            )
            val publicKeyCredential = credentialResponse.credential as PublicKeyCredential

            val completeInputModel = LoginCompleteRequest(
                session = beginResponseData.session,
                response = PublicKeyCredentialConverter.convertJson(publicKeyCredential.authenticationResponseJson),
                origin = _options.origin,
                rpId = _options.rpId
            )
            val completeResponse = _httpClient
                .loginComplete(completeInputModel)
                .throwIfNetworkRequestFailed()

            onLoginResult(true, null, completeResponse.body()!!)
        } catch (e: Exception) {
            Log.e("passwordless-login-begin", "Cannot initiate login request", e)
            withContext(Dispatchers.Main) {
                onLoginResult(false, e, null)
            }
        }
    }

    /**
     * Initiates the registration process for Passwordless authentication.
     *
     * @param token The registration token obtained from the backend.
     * @param nickname The nickname for the credential.
     * @param onRegisterResult Callback function to handle the registration result.
     *                         It provides a boolean indicating success, an optional exception in case of failure,
     *                         and the response containing information about the completed registration if successful.
     */

    fun register(
        token: String,
        nickname: String = "",
        onRegisterResult: (Boolean, Exception?, RegisterCompleteResponse?) -> Unit
    ) = _coroutineScope.launch(Dispatchers.IO) {
        try {
            val beginInputModel = RegisterBeginRequest(
                token = token,
                rpId = _options.rpId,
                origin = _options.origin
            )

            val beginResponse =
                _httpClient
                    .registerBegin(beginInputModel)
                    .throwIfNetworkRequestFailed()


            val beginResult: RegisterBeginResponse = beginResponse.body()!!

            val response = credentialManager.createCredential(
                _context,
                beginResult.data
            ) as CreatePublicKeyCredentialResponse

            val completeRequest = RegisterCompleteRequest(
                session = beginResult.session,
                response = PublicKeyCredentialConverter.convertJson(response.registrationResponseJson),
                nickname = nickname,
                origin = _options.origin,
                rpId = _options.rpId
            )
            val registerCompleteResponse =
                _httpClient.registerComplete(completeRequest).throwIfNetworkRequestFailed()

            withContext(Dispatchers.Main) {
                onRegisterResult(true, null, registerCompleteResponse.body())
            }
        } catch (e: Exception) {
            Log.e("passwordless-register-begin", "Cannot call registerRequest", e)
            withContext(Dispatchers.Main) {
                onRegisterResult(false, e, null)
            }
        }
    }
}
