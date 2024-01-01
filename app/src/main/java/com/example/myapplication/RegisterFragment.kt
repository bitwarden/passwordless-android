package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.myapplication.databinding.FragmentRegisterBinding
import com.example.myapplication.services.yourbackend.YourBackendHttpClient
import com.example.myapplication.services.yourbackend.contracts.UserRegisterRequest
import dagger.hilt.android.AndroidEntryPoint
import dev.passwordless.android.PasswordlessClient
import dev.passwordless.android.utils.PasswordlessUtils.Companion.getPasskeyFailureMessage
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null

    @Inject
    lateinit var _passwordless: PasswordlessClient

    @Inject
    lateinit var httpClient: YourBackendHttpClient

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        //Scope needs to be updated according to current class
        _passwordless
            .setCoroutineScope(lifecycleScope)

        handleLoginNavigation()
        return binding.root
    }

    private fun handleLoginNavigation() {
        binding.loginTV.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonRegister.setOnClickListener {
            lifecycleScope.launch {
                val alias = binding.aliasEditText.text.toString()
                val username = binding.usernameEditText.text.toString()
                try {
                    val responseToken =
                        httpClient.register(UserRegisterRequest(username, alias)).body()?.token!!
                    _passwordless.register(
                        responseToken,
                        alias + username
                    ) { success, exception, result ->
                        if (success) {
                            Toast.makeText(context, result.toString(), Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(
                                context,
                                "Exception: " + getPasskeyFailureMessage(exception as Exception),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } catch (e: Exception) {
                    Toast.makeText(context, e.message.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
