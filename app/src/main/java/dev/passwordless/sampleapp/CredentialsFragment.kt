package dev.passwordless.sampleapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import dev.passwordless.android.PasswordlessClient
import dev.passwordless.sampleapp.databinding.FragmentCredentialsBinding
import javax.inject.Inject

@AndroidEntryPoint
class CredentialsFragment : Fragment() {

    private var _binding: FragmentCredentialsBinding? = null

    @Inject
    lateinit var _passwordless: PasswordlessClient

    @Inject
    lateinit var httpClient: YourBackendHttpClient

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentCredentialsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
