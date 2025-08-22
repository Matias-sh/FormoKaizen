package com.cocido.formokaizen.presentation.ui.fragments

import android.content.Intent
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
import com.cocido.formokaizen.databinding.FragmentProfileBinding
import com.cocido.formokaizen.domain.entities.User
import com.cocido.formokaizen.domain.entities.UserRole
import com.cocido.formokaizen.presentation.viewmodel.TarjetasViewModel
import com.cocido.formokaizen.presentation.viewmodels.AuthViewModel
import com.cocido.formokaizen.utils.Resource
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    
    private val authViewModel: AuthViewModel by viewModels()
    private val tarjetasViewModel: TarjetasViewModel by viewModels()
    
    private var currentUser: User? = null
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupClickListeners()
        observeViewModel()
        loadUserProfile()
        loadUserStats()
    }
    
    private fun setupClickListeners() {
        binding.btnMyTarjetas.setOnClickListener {
            // Navegar a Tarjetas fragment con filtro "Mis tarjetas"
            findNavController().navigate(R.id.tarjetasFragment)
            // TODO: Implementar filtro automático para mostrar solo mis tarjetas
        }
        
        binding.btnEditProfile.setOnClickListener {
            // TODO: Implementar edición de perfil
            Toast.makeText(context, "Función de editar perfil en desarrollo", Toast.LENGTH_SHORT).show()
        }
        
        binding.btnChangePassword.setOnClickListener {
            // TODO: Implementar cambio de contraseña
            Toast.makeText(context, "Función de cambio de contraseña en desarrollo", Toast.LENGTH_SHORT).show()
        }
        
        binding.btnLogout.setOnClickListener {
            showLogoutConfirmationDialog()
        }
        
        binding.btnRetry.setOnClickListener {
            loadUserProfile()
            loadUserStats()
        }
    }
    
    private fun loadUserProfile() {
        authViewModel.getCurrentUser()
    }
    
    private fun loadUserStats() {
        // Cargar estadísticas del usuario
        tarjetasViewModel.loadMyTarjetas()
    }
    
    private fun observeViewModel() {
        // Observar el estado del usuario actual
        viewLifecycleOwner.lifecycleScope.launch {
            authViewModel.currentUser.collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        showLoading(true)
                        showError(false)
                    }
                    is Resource.Success -> {
                        showLoading(false)
                        showError(false)
                        resource.data?.let { user ->
                            currentUser = user
                            updateUserInfo(user)
                        }
                    }
                    is Resource.Error -> {
                        showLoading(false)
                        showError(true, resource.message)
                    }
                    else -> {
                        showLoading(false)
                        showError(false)
                    }
                }
            }
        }
        
        // Observar las tarjetas del usuario para estadísticas
        viewLifecycleOwner.lifecycleScope.launch {
            tarjetasViewModel.tarjetas.collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        val myTarjetas = resource.data ?: emptyList()
                        updateUserStats(myTarjetas)
                    }
                    else -> {
                        // No actualizar estadísticas en caso de error
                    }
                }
            }
        }
        
        // Observar el estado de logout
        viewLifecycleOwner.lifecycleScope.launch {
            authViewModel.logoutState.collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        // Mostrar loading en el botón de logout
                        binding.btnLogout.isEnabled = false
                    }
                    is Resource.Success -> {
                        binding.btnLogout.isEnabled = true
                        // Navegar a login
                        navigateToLogin()
                    }
                    is Resource.Error -> {
                        binding.btnLogout.isEnabled = true
                        Toast.makeText(
                            context,
                            resource.message ?: "Error al cerrar sesión",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    else -> {
                        binding.btnLogout.isEnabled = true
                    }
                }
            }
        }
    }
    
    private fun updateUserInfo(user: User) {
        binding.apply {
            tvFullName.text = user.fullName
            tvEmail.text = user.email
            tvRole.text = when (user.role) {
                UserRole.ADMIN -> "Administrador"
                UserRole.SUPERVISOR -> "Supervisor"
                UserRole.USER -> "Usuario"
            }
            
            // Mostrar departamento si existe
            if (!user.department.isNullOrBlank()) {
                tvDepartment.text = user.department
                tvDepartment.visibility = View.VISIBLE
            } else {
                tvDepartment.visibility = View.GONE
            }
            
            // Mostrar Employee ID si existe
            if (!user.employeeId.isNullOrBlank()) {
                tvEmployeeId.text = user.employeeId
                llEmployeeId.visibility = View.VISIBLE
            } else {
                llEmployeeId.visibility = View.GONE
            }
            
            // Mostrar teléfono si existe
            if (!user.phone.isNullOrBlank()) {
                tvPhone.text = user.phone
                llPhone.visibility = View.VISIBLE
            } else {
                llPhone.visibility = View.GONE
            }
            
            // Mostrar posición si existe
            if (!user.position.isNullOrBlank()) {
                tvPosition.text = user.position
                llPosition.visibility = View.VISIBLE
            } else {
                llPosition.visibility = View.GONE
            }
            
            // TODO: Cargar avatar si existe
            // Glide.with(this@ProfileFragment).load(user.avatar).into(ivAvatar)
        }
    }
    
    private fun updateUserStats(myTarjetas: List<com.cocido.formokaizen.domain.entities.TarjetaRoja>) {
        val totalCount = myTarjetas.size
        val resolvedCount = myTarjetas.count { 
            it.status in listOf(
                com.cocido.formokaizen.domain.entities.TarjetaStatus.RESOLVED,
                com.cocido.formokaizen.domain.entities.TarjetaStatus.CLOSED
            )
        }
        val pendingCount = myTarjetas.count { 
            it.status in listOf(
                com.cocido.formokaizen.domain.entities.TarjetaStatus.OPEN,
                com.cocido.formokaizen.domain.entities.TarjetaStatus.PENDING_APPROVAL,
                com.cocido.formokaizen.domain.entities.TarjetaStatus.IN_PROGRESS
            )
        }
        
        binding.apply {
            tvMyTarjetasCount.text = totalCount.toString()
            tvMyResolvedCount.text = resolvedCount.toString()
            tvMyPendingCount.text = pendingCount.toString()
        }
    }
    
    private fun showLogoutConfirmationDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Cerrar Sesión")
            .setMessage("¿Estás seguro de que quieres cerrar sesión?")
            .setPositiveButton("Cerrar Sesión") { _, _ ->
                authViewModel.logout()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
    
    private fun navigateToLogin() {
        // Navegar de vuelta al flujo de login
        activity?.finish()
    }
    
    private fun showLoading(show: Boolean) {
        binding.apply {
            llLoading.visibility = if (show) View.VISIBLE else View.GONE
            cardHeader.visibility = if (show) View.GONE else View.VISIBLE
            cardPersonalInfo.visibility = if (show) View.GONE else View.VISIBLE
            cardStats.visibility = if (show) View.GONE else View.VISIBLE
            cardActions.visibility = if (show) View.GONE else View.VISIBLE
        }
    }
    
    private fun showError(show: Boolean, message: String? = null) {
        binding.apply {
            llErrorState.visibility = if (show) View.VISIBLE else View.GONE
            cardHeader.visibility = if (show) View.GONE else View.VISIBLE
            cardPersonalInfo.visibility = if (show) View.GONE else View.VISIBLE
            cardStats.visibility = if (show) View.GONE else View.VISIBLE
            cardActions.visibility = if (show) View.GONE else View.VISIBLE
            
            if (show && message != null) {
                tvErrorMessage.text = message
            }
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}