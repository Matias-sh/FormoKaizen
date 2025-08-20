package com.cocido.formokaizen.presentation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.cocido.formokaizen.R
import com.cocido.formokaizen.databinding.FragmentRegisterBinding
import com.cocido.formokaizen.domain.entities.RegisterRequest
import com.cocido.formokaizen.presentation.viewmodels.AuthViewModel
import com.cocido.formokaizen.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterFragment : Fragment() {
    
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    
    private val authViewModel: AuthViewModel by viewModels()
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupClickListeners()
        observeAuthState()
    }
    
    private fun setupClickListeners() {
        binding.btnRegister.setOnClickListener {
            performRegister()
        }
        
        binding.btnBackToLogin.setOnClickListener {
            findNavController().navigate(R.id.action_register_to_login)
        }
    }
    
    private fun performRegister() {
        val firstName = binding.etFirstName.text.toString().trim()
        val lastName = binding.etLastName.text.toString().trim()
        val username = binding.etUsername.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString()
        val confirmPassword = binding.etConfirmPassword.text.toString()
        
        // Validate input
        if (!validateInput(firstName, lastName, username, email, password, confirmPassword)) {
            return
        }
        
        // Clear previous errors
        clearErrors()
        
        // Create register request
        val registerRequest = RegisterRequest(
            username = username,
            email = email,
            password = password,
            passwordConfirm = confirmPassword,
            firstName = firstName,
            lastName = lastName,
            employeeId = "", // Optional field
            phone = "", // Optional field  
            department = "", // Optional field
            position = "" // Optional field
        )
        
        // Perform registration
        authViewModel.register(registerRequest)
    }
    
    private fun validateInput(
        firstName: String,
        lastName: String,
        username: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        var isValid = true
        
        if (firstName.isEmpty()) {
            binding.tilFirstName.error = "El nombre es requerido"
            isValid = false
        }
        
        if (lastName.isEmpty()) {
            binding.tilLastName.error = "El apellido es requerido"
            isValid = false
        }
        
        if (username.isEmpty()) {
            binding.tilUsername.error = "El usuario es requerido"
            isValid = false
        }
        
        if (email.isEmpty()) {
            binding.tilEmail.error = "El email es requerido"
            isValid = false
        }
        
        if (password.isEmpty()) {
            binding.tilPassword.error = "La contraseña es requerida"
            isValid = false
        } else if (password.length < 6) {
            binding.tilPassword.error = "La contraseña debe tener al menos 6 caracteres"
            isValid = false
        }
        
        if (confirmPassword.isEmpty()) {
            binding.tilConfirmPassword.error = "Confirmar contraseña es requerido"
            isValid = false
        } else if (password != confirmPassword) {
            binding.tilConfirmPassword.error = "Las contraseñas no coinciden"
            isValid = false
        }
        
        return isValid
    }
    
    private fun clearErrors() {
        binding.tilFirstName.error = null
        binding.tilLastName.error = null
        binding.tilUsername.error = null
        binding.tilEmail.error = null
        binding.tilPassword.error = null
        binding.tilConfirmPassword.error = null
    }
    
    private fun observeAuthState() {
        viewLifecycleOwner.lifecycleScope.launch {
            authViewModel.authState.collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        showLoading(true)
                    }
                    is Resource.Success -> {
                        showLoading(false)
                        Toast.makeText(context, "¡Registro exitoso! Bienvenido.", Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_register_to_home)
                    }
                    is Resource.Error -> {
                        showLoading(false)
                        Toast.makeText(context, resource.message ?: "Error en el registro", Toast.LENGTH_LONG).show()
                    }
                    else -> {
                        showLoading(false)
                    }
                }
            }
        }
    }
    
    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.btnRegister.isEnabled = !isLoading
        binding.btnBackToLogin.isEnabled = !isLoading
        
        // Disable all input fields
        binding.etFirstName.isEnabled = !isLoading
        binding.etLastName.isEnabled = !isLoading
        binding.etUsername.isEnabled = !isLoading
        binding.etEmail.isEnabled = !isLoading
        binding.etPassword.isEnabled = !isLoading
        binding.etConfirmPassword.isEnabled = !isLoading
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}