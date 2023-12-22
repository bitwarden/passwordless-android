package dev.passwordless.android

import android.app.Activity
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.fido.Fido
import com.google.android.gms.fido.fido2.Fido2ApiClient
import com.google.android.gms.fido.fido2.api.common.AuthenticatorErrorResponse
import com.google.android.gms.fido.fido2.api.common.PublicKeyCredential
import com.google.android.gms.fido.fido2.api.common.PublicKeyCredentialCreationOptions
import com.google.android.gms.fido.fido2.api.common.PublicKeyCredentialRpEntity
import com.google.android.gms.fido.fido2.api.common.PublicKeyCredentialUserEntity
import dev.passwordless.android.rest.PasswordlessHttpClient
import dev.passwordless.android.rest.PasswordlessHttpClientFactory
import dev.passwordless.android.rest.PasswordlessOptions
import dev.passwordless.android.rest.contracts.RegisterBeginRequest
import dev.passwordless.android.rest.contracts.RegisterBeginResponse
import dev.passwordless.android.rest.contracts.RegisterCompleteRequest
import dev.passwordless.android.rest.contracts.getPublicKeyCredentialParameters
import dev.passwordless.android.rest.converters.PublicKeyCredentialConverter
import dev.passwordless.android.rest.exceptions.PasswordlessApiException
import dev.passwordless.android.rest.exceptions.PasswordlessCredentialCreateException
import dev.passwordless.android.rest.exceptions.ProblemDetails
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

/**
 * Manages the communication with the Fido2 API and performs registration operations for the Passwordless authentication flow.
 *
 * @param fido2ApiClient The Fido2ApiClient instance for interacting with the Fido2 API.
 * @param options The configuration options for Passwordless authentication.
 * @param activity The ComponentActivity associated with the PasswordlessClient for launching the registration intent.
 * @param externalScope The CoroutineScope for launching coroutines related to Passwordless operations (optional).
 * If this scope is not provided, then call the cancel function in onDestroy method.
 * @param onHandleRegistrationResult Callback function to handle the result of the registration process.
 */
class PasswordlessClient(
    fido2ApiClient: Fido2ApiClient,
    options: PasswordlessOptions,
    activity: ComponentActivity,
    externalScope: CoroutineScope? = null,
    private val onHandleRegistrationResult: (Boolean, Any?) -> Unit
) {
    private val _fido2ApiClient: Fido2ApiClient = fido2ApiClient
    private val _httpClient: PasswordlessHttpClient
    private val _options: PasswordlessOptions = options
    private val _externalScope = externalScope ?: CoroutineScope(Dispatchers.Main)


    private val _createCredentialIntentLauncher = activity.registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult(),
        ::handleRegistration
    )

    /**
     * Stores data related to the current registration request, specifically the session ID and nickname.
     */
    private var _currentRequestData: Pair<String, String>? = null

    /**
     * A flag used to ensure only one registration request is processed at a time until completion.
     * When set to `true`, it indicates that a registration request is already in progress.
     * When set to `false`, a new registration request can be initiated.
     */
    private var _lock = false

    /**
     * Provides the status of any ongoing registration request.
     *
     * - `true`: Indicates that a previous registration request is already in progress.
     *    A new request should wait until the ongoing one completes or fails.
     *
     * - `false`: Indicates that no registration request is currently in progress.
     *    A new registration request can be initiated.
     */
    val isRegistrationInProgress
        get() = _lock


    init {
        _httpClient = PasswordlessHttpClientFactory.create(options)
    }

    /**
     * Initiates the registration process for Passwordless authentication.
     *
     * @param token The registration token obtained from the backend.
     * @param nickname The nickname for the credential.
     * @param onRegisterResult Callback function to handle the registration result.
     */
    fun register(
        token: String,
        nickname: String = "",
        onRegisterResult: (Boolean, Exception?) -> Unit
    ) = _externalScope.launch {
        synchronized(this) {
            if (_lock) {
                onRegisterResult(false, Exception("One request already in progress"))
                return@launch
            }
            _lock = true
        }
        withContext(Dispatchers.IO) {
            try {
                val beginInputModel = RegisterBeginRequest(
                    token = token,
                    rpId = _options.rpId,
                    origin = _options.origin
                )

                val beginResponse = _httpClient.registerBegin(beginInputModel)

                if (!beginResponse.isSuccessful) {
                    val problemDetails =
                        ProblemDetails("", beginResponse.message(), beginResponse.code(), "")
                    throw PasswordlessApiException(problemDetails)
                }

                val beginResult: RegisterBeginResponse = beginResponse.body()!!
                _currentRequestData = Pair(beginResult.session, nickname)

                val credentialCreationOptions = publicKeyCredentialCreationOptions(beginResult)

                val createdCredential =
                    _fido2ApiClient.getRegisterPendingIntent(credentialCreationOptions)
                        .await()
                        ?: throw Exception("Creation intent is null")

                _createCredentialIntentLauncher.launch(
                    IntentSenderRequest.Builder(
                        createdCredential
                    ).build()
                )

                withContext(Dispatchers.Main) {
                    onRegisterResult(true, null)
                }
            } catch (e: Exception) {
                Log.e("passwordless-register-begin", "Cannot call registerRequest", e)
                synchronized(this) {
                    _currentRequestData = null
                    _lock = false
                }
                withContext(Dispatchers.Main) {
                    onRegisterResult(false, e)
                }
            }
        }
    }

    private fun publicKeyCredentialCreationOptions(beginResult: RegisterBeginResponse): PublicKeyCredentialCreationOptions =
        beginResult.data.run {
            PublicKeyCredentialCreationOptions
                .Builder()
                .setChallenge(challenge)
                .setTimeoutSeconds(timeout.toDouble())
                .setRp(PublicKeyCredentialRpEntity(rp.id, rp.name, null))
                .setUser(PublicKeyCredentialUserEntity(user.id, user.name, "", user.displayName))
                .setParameters(getPublicKeyCredentialParameters())
                .build()
        }


    /**
     * Handles the result of the registration process when the credential creation intent returns.
     *
     * @param activityResult The result of the registration intent.
     */
    private fun handleRegistration(activityResult: ActivityResult) =
        _externalScope.launch(Dispatchers.IO) {
            try {
                val bytes = activityResult.data?.getByteArrayExtra(Fido.FIDO2_KEY_CREDENTIAL_EXTRA)
                val sessionId = _currentRequestData!!.first
                val nickname = _currentRequestData!!.second
                when {
                    (activityResult.resultCode != Activity.RESULT_OK || bytes == null) ->
                        throw PasswordlessCredentialCreateException("Failed to create credential.")

                    else -> {
                        val credential = PublicKeyCredential.deserializeFromBytes(bytes)
                        if (credential.response is AuthenticatorErrorResponse) {
                            val errorResponse = credential.response as AuthenticatorErrorResponse
                            throw PasswordlessCredentialCreateException(errorResponse.errorMessage)
                        } else {
                            val request = RegisterCompleteRequest(
                                session = sessionId,
                                response = PublicKeyCredentialConverter.convertJson(credential.toJson()),
                                nickname = nickname,
                                origin = _options.origin,
                                rpId = _options.rpId
                            )
                            val response = _httpClient.registerComplete(request)
                            if (response.isSuccessful) {
                                withContext(Dispatchers.Main) {
                                    onHandleRegistrationResult(true, response.body())
                                }
                            } else {
                                throw PasswordlessCredentialCreateException("Network call failed")
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onHandleRegistrationResult(false, e)
                }
            } finally {
                synchronized(this) {
                    _currentRequestData = null
                    _lock = false
                }
            }
        }

    /**
     * Cancel all ongoing coroutines in the scope.
     * If no scope is provided then this should be called in onDestroy
     */
    fun cancel() {
        _externalScope.cancel()
    }
}
