package com.cocido.formokaizen.presentation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.cocido.formokaizen.R
import com.cocido.formokaizen.databinding.FragmentHomeBinding
import com.cocido.formokaizen.domain.entities.TarjetaStatus
import com.cocido.formokaizen.presentation.viewmodel.TarjetasViewModel
import com.cocido.formokaizen.presentation.viewmodels.AuthViewModel
import com.cocido.formokaizen.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {
    
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    
    private val authViewModel: AuthViewModel by viewModels()
    private val tarjetasViewModel: TarjetasViewModel by viewModels()
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupClickListeners()
        loadUserData()
        loadStats()
    }
    
    private fun setupClickListeners() {
        binding.btnNewTarjeta.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_create_tarjeta)
        }
        
        binding.btnViewTarjetas.setOnClickListener {
            findNavController().navigate(R.id.tarjetasFragment)
        }
    }
    
    private fun loadUserData() {
        viewLifecycleOwner.lifecycleScope.launch {
            // Only get current user if logged in
            if (authViewModel.isLoggedIn()) {
                authViewModel.getCurrentUser()
            }
        }
    }
    
    private fun loadStats() {
        // Cargar estadísticas reales desde el ViewModel
        tarjetasViewModel.loadTarjetas()
        observeStats()
    }
    
    private fun observeStats() {
        viewLifecycleOwner.lifecycleScope.launch {
            tarjetasViewModel.tarjetas.collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        val tarjetas = resource.data ?: emptyList()
                        updateStats(tarjetas)
                    }
                    else -> {
                        // En caso de error, mostrar 0
                        binding.tvTotalTarjetas.text = "0"
                        binding.tvOpenTarjetas.text = "0"
                        binding.tvResolvedTarjetas.text = "0"
                    }
                }
            }
        }
    }
    
    private fun updateStats(tarjetas: List<com.cocido.formokaizen.domain.entities.TarjetaRoja>) {
        val total = tarjetas.size
        val abiertas = tarjetas.count { 
            it.status in listOf(
                TarjetaStatus.OPEN,
                TarjetaStatus.PENDING_APPROVAL,
                TarjetaStatus.IN_PROGRESS
            )
        }
        val resueltas = tarjetas.count { 
            it.status in listOf(
                TarjetaStatus.RESOLVED,
                TarjetaStatus.CLOSED
            )
        }
        
        binding.tvTotalTarjetas.text = total.toString()
        binding.tvOpenTarjetas.text = abiertas.toString()
        binding.tvResolvedTarjetas.text = resueltas.toString()
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}