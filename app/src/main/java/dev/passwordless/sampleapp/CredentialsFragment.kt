package dev.passwordless.sampleapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.passwordless.android.PasswordlessClient
import dev.passwordless.sampleapp.auth.Session
import dev.passwordless.sampleapp.databinding.FragmentCredentialsBinding
import dev.passwordless.sampleapp.yourbackend.YourBackendHttpClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class CredentialsFragment : Fragment() {
    @Inject lateinit var httpClient: YourBackendHttpClient
    @Inject lateinit var session: Session
    @Inject lateinit var passwordless: PasswordlessClient

    private var _binding: FragmentCredentialsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCredentialsBinding.inflate(inflater, container, false)

        lifecycleScope.launch {
            val credentialsResponse = withContext(Dispatchers.IO) {
                httpClient.getCredentials(session.getUserId()!!)
            }

            if (credentialsResponse.isSuccessful) {
                binding.credentialsList.adapter = CredentialAdapter(requireContext(), credentialsResponse.body()!!)
            } else {
                Toast.makeText(context, credentialsResponse.message(), Toast.LENGTH_SHORT).show()
            }
        }

        if (!session.isLoggedIn()) {
            findNavController().navigate(R.id.action_credentials_to_login_fragment)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addCredentialButton.setOnClickListener {
            findNavController().navigate(R.id.action_credentials_to_add_credential_fragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
