package dev.passwordless.sampleapp;

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.passwordless.android.PasswordlessClient
import dev.passwordless.android.utils.PasswordlessUtils
import dev.passwordless.sampleapp.auth.Session
import dev.passwordless.sampleapp.contracts.UserRegisterRequest
import dev.passwordless.sampleapp.databinding.FragmentAddCredentialBinding
import dev.passwordless.sampleapp.yourbackend.YourBackendHttpClient
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AddCredentialFragment : Fragment() {
    @Inject
    lateinit var httpClient: YourBackendHttpClient
    @Inject
    lateinit var session: Session
    @Inject
    lateinit var passwordless: PasswordlessClient

    private var _binding: FragmentAddCredentialBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddCredentialBinding.inflate(inflater, container, false)

        if (!session.isLoggedIn()) {
            val intent = Intent(context, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            requireContext().startActivity(intent)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonRegister.setOnClickListener {
            lifecycleScope.launch {
                val alias = binding.aliasEditText.text.toString()
                val username = session.getUsername()!!
                try {
                    val response =
                        httpClient.register(UserRegisterRequest(username, alias)).body()?.token!!
                    passwordless.register(
                        response,
                        alias + username
                    ) { success, exception, result ->
                        if (success) {
                            findNavController().navigate(R.id.action_add_credential_to_credentials_fragment)
                        } else {
                            Toast.makeText(
                                context,
                                "Exception: " + PasswordlessUtils.getPasskeyFailureMessage(exception as Exception),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } catch (e: Exception) {
                    Toast.makeText(context, e.message.toString(), Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
