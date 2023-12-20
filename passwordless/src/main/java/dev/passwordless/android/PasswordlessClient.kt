package dev.passwordless.android

import android.app.Activity
import android.app.PendingIntent
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.FragmentActivity
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
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await

class PasswordlessClient(
    fido2ApiClient: Fido2ApiClient?,
    options: PasswordlessOptions,
    activity: FragmentActivity?
) {
    private val _fido2ApiClient: Fido2ApiClient? = fido2ApiClient
    private val _httpClient: PasswordlessHttpClient
    private val _options: PasswordlessOptions = options

    // request scoped start
    private var _nickname: String? = null
    // request scoped end
    private val createCredentialIntentLauncher = activity?.registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult(),
        ::handleRegistration
    )
    private var sessionId:String?=null
    init {
        _httpClient = PasswordlessHttpClientFactory.create(options)
    }

    /**
     * @param token The registration token obtained from your backend
     * @param nickname The nickname for the credential
     * @return
     */
    suspend fun register(token: String, nickname: String? = null): PendingIntent? {
        _nickname = nickname

        val beginInputModel = RegisterBeginRequest(
            token = token,
            rpId = _options.rpId,
            origin = _options.origin
        )

        _fido2ApiClient?.let { _ ->
            try {
                val beginResponse = _httpClient.registerBegin(beginInputModel)
                if (!beginResponse.isSuccessful) {
                    val problemDetails = ProblemDetails("", beginResponse.message(), beginResponse.code(), "")
                    throw PasswordlessApiException(problemDetails)
                }
                val beginResult: RegisterBeginResponse = beginResponse.body()!!
                sessionId = beginResult.session


                val credentialCreationOptions = PublicKeyCredentialCreationOptions
                    .Builder()
                    .setChallenge(beginResult.data.challenge)
                    .setTimeoutSeconds(beginResult.data.timeout.toDouble())
                    .setRp(PublicKeyCredentialRpEntity(beginResult.data.rp.id,beginResult.data.rp.name,null))
                    .setUser(PublicKeyCredentialUserEntity(beginResult.data.user.id,beginResult.data.user.name,"",beginResult.data.user.displayName))
                    .setParameters(beginResult.data.getPublicKeyCredentialParameters())
                    .build()

                val creationIntent = _fido2ApiClient.getRegisterPendingIntent(credentialCreationOptions)

                val createdCredential = creationIntent.await()
                createCredentialIntentLauncher!!.launch(
                    IntentSenderRequest.Builder(createdCredential).build()
                )
            } catch (e: Exception) {
                Log.e("passwordless-register-begin", "Cannot call registerRequest", e)
            }
        }
        return null
    }

    /**
     * @param activityResult
     */
    fun handleRegistration(activityResult: ActivityResult) {
        val bytes = activityResult.data?.getByteArrayExtra(Fido.FIDO2_KEY_CREDENTIAL_EXTRA)
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
                        session = sessionId!!,
                        response = PublicKeyCredentialConverter.convertJson(credential.toJson()),
                        nickname = _nickname,
                        origin = _options.origin,
                        rpId = _options.rpId
                    )
                    // todo: need to remove runBlocking
                    runBlocking {  _httpClient.registerComplete(request)}

                }
            }
        }
    }
}
