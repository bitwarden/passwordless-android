package dev.passwordless.android

import android.content.Context
import android.util.Log
import androidx.credentials.CreatePublicKeyCredentialResponse
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.PublicKeyCredential
import androidx.credentials.exceptions.CreateCredentialException
import androidx.credentials.exceptions.GetCredentialException
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
import dev.passwordless.android.rest.exceptions.PasswordlessApiException
import dev.passwordless.android.rest.exceptions.ProblemDetails
import dev.passwordless.android.utils.SignatureService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

/**
 * Manages the communication with the Credential Manager API and performs registration operations
 * for the Passwordless authentication flow.
 * @param options The configuration options for Passwordless authentication.
 */
class PasswordlessClient(
    private val options: PasswordlessOptions,
) {
    private val httpClient: PasswordlessHttpClient = PasswordlessHttpClientFactory.create(options)

    private lateinit var coroutineScope: CoroutineScope
    private lateinit var credentialManager: CredentialManager
    private lateinit var context: Context
    private lateinit var signatureService: SignatureService

    /**
     * Manages the communication with the Credential Manager API and performs registration
     * operations for the Passwordless authentication flow.
     *
     * @param options The configuration options for Passwordless authentication.
     * @param context The context of the activity.
     * @param scope The coroutine scope of the activity.
     */
    constructor(
        options: PasswordlessOptions,
        context: Context,
        scope: CoroutineScope,
    ) : this(options) {
        setContext(context)
        setCoroutineScope(scope)
    }

    /**
     * Sets the coroutine scope of the activity.
     *
     * @param coroutineScope The coroutine scope of the activity.
     * @return The PasswordlessClient instance.
     * @throws IllegalStateException If the coroutine scope is set more than once.
     */
    fun setCoroutineScope(coroutineScope: CoroutineScope): PasswordlessClient =
        apply {
            if (::coroutineScope.isInitialized) {
                throw IllegalStateException("CoroutineScope cannot be set more than once")
            }
            this.coroutineScope = coroutineScope
        }

    /**
     * Sets the context of the activity.
     *
     * @param context The context of the activity.
     * @return The PasswordlessClient instance.
     * @throws IllegalStateException If the context is set more than once.
     */
    fun setContext(context: Context): PasswordlessClient =
        apply {
            if (::context.isInitialized) {
                throw IllegalStateException("Context cannot be set more than once")
            }
            this.context = context
            signatureService = SignatureService(context)
            credentialManager = CredentialManager.create(context)
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
     * @param onLoginResult Callback function to handle the login result. It provides a boolean
     * indicating success, an optional exception in case of failure, and the response containing
     * information about the completed login if successful.
     */
    fun login(
        alias: String,
        onLoginResult: (Boolean, Exception?, LoginCompleteResponse?) -> Unit,
    ) = coroutineScope.launch(Dispatchers.IO) {
        try {
            val beginInputModel = LoginBeginRequest(
                alias = alias,
                rpId = options.rpId,
                origin = signatureService.getFacetId(),
            )
            val beginResponse = httpClient
                .loginBegin(beginInputModel)
                .throwIfNetworkRequestFailed()

            val beginResponseData: LoginBeginResponse = beginResponse.body()!!

            val credentialResponse = credentialManager.getCredential(
                context,
                GetCredentialRequest(
                    listOf(
                        beginResponseData.data,
                    ),
                ),
            )

            val completeInputModel = LoginCompleteRequest(
                session = beginResponseData.session,
                response = credentialResponse.credential as PublicKeyCredential,
                origin = signatureService.getFacetId(),
                rpId = options.rpId,
            )

            val completeResponse = httpClient
                .loginComplete(completeInputModel)
                .throwIfNetworkRequestFailed()

            onLoginResult(true, null, completeResponse.body()!!)
        } catch (e: PasswordlessApiException) {
            Log.e("bwp-login", "Unexpectedly failed logging in with FIDO2 credential.", e)
            withContext(Dispatchers.Main) {
                onLoginResult(false, e, null)
            }
        } catch (e: GetCredentialException) {
            Log.e("bwp-login", "Unexpectedly failed logging in with FIDO2 credential.", e)
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
     * @param onRegisterResult Callback function to handle the registration result. It provides a
     * boolean indicating success, an optional exception in case of failure, and the response
     * containing information about the completed registration if successful.
     */
    fun register(
        token: String,
        nickname: String = "",
        onRegisterResult: (Boolean, Exception?, RegisterCompleteResponse?) -> Unit,
    ) = coroutineScope.launch(Dispatchers.IO) {
        try {
            val beginInputModel = RegisterBeginRequest(
                token = token,
                rpId = options.rpId,
                origin = signatureService.getFacetId(),
            )

            val beginResponse =
                httpClient
                    .registerBegin(beginInputModel)
                    .throwIfNetworkRequestFailed()

            val beginResult: RegisterBeginResponse = beginResponse.body()!!

            val response = credentialManager.createCredential(
                context,
                beginResult.data,
            ) as CreatePublicKeyCredentialResponse

            val completeRequest = RegisterCompleteRequest(
                session = beginResult.session,
                response = response,
                nickname = nickname,
                origin = signatureService.getFacetId(),
                rpId = options.rpId,
            )

            val completeResponse =
                httpClient.registerComplete(completeRequest).throwIfNetworkRequestFailed()

            withContext(Dispatchers.Main) {
                onRegisterResult(true, null, completeResponse.body())
            }
        } catch (e: PasswordlessApiException) {
            Log.e("bwp-register", "Unexpectedly failed creating FIDO2 credential.", e)
            withContext(Dispatchers.Main) {
                onRegisterResult(false, e, null)
            }
        } catch (e: CreateCredentialException) {
            Log.e("bwp-register", "Unexpectedly failed creating FIDO2 credential.", e)
            withContext(Dispatchers.Main) {
                onRegisterResult(false, e, null)
            }
        }
    }
}
