package com.example.myapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.databinding.FragmentRegisterBinding
import com.example.myapplication.services.yourbackend.YourBackendHttpClientFactory
import com.example.myapplication.services.yourbackend.config.DemoPasswordlessOptions
import com.example.myapplication.services.yourbackend.contracts.UserRegisterRequest
import com.google.android.gms.fido.Fido
import dev.passwordless.android.PasswordlessClient
import dev.passwordless.android.rest.PasswordlessOptions
import kotlinx.coroutines.launch

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null

    private lateinit var _passwordless: PasswordlessClient

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {

        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        val fido2ApiClient = Fido.getFido2ApiClient(this.requireContext().applicationContext)

        val options = PasswordlessOptions(
            DemoPasswordlessOptions.API_KEY,
            DemoPasswordlessOptions.RP_ID,
            DemoPasswordlessOptions.ORIGIN,
            DemoPasswordlessOptions.API_URL)

        _passwordless = PasswordlessClient(fido2ApiClient, options,this.requireActivity(),lifecycleScope) { success, result ->
            // If the registration is successful, then we will get the final result here
            if (success) {
                Toast.makeText(context, "Registration successful: $result", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Registration failed: $result", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonRegister.setOnClickListener {
            if(_passwordless.isRegistrationInProgress){
                Toast.makeText(context, "Please wait!!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            lifecycleScope.launch {
                val httpClient = YourBackendHttpClientFactory.create(DemoPasswordlessOptions.YOUR_BACKEND_URL)
                val alias = binding.aliasEditText.text.toString()
                val username = binding.usernameEditText.text.toString()
                try {
                    val responseToken = httpClient.register(UserRegisterRequest(username,alias)).body()?.token!!
                    _passwordless.register(responseToken,alias+username) { success , exception ->
                        // This is called when we got the response for register/begin and fido2 intent has started
                        // Note: We do not get the final result here.
                        if (success) {
                            Toast.makeText(context,"Registration started", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Exception: "+exception?.message.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                }catch (e: Exception){
                    Toast.makeText(context, e.message.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _passwordless.cancel()
        _binding = null
    }
}
