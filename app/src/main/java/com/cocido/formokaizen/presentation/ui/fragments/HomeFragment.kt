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
import com.cocido.formokaizen.presentation.viewmodels.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {
    
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    
    private val authViewModel: AuthViewModel by viewModels()
    
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
            // Get current user and update welcome message
            authViewModel.getCurrentUser()
        }
    }
    
    private fun loadStats() {
        // TODO: Load statistics from TarjetasViewModel
        // For now, show placeholder data
        binding.tvTotalTarjetas.text = "12"
        binding.tvOpenTarjetas.text = "5"
        binding.tvResolvedTarjetas.text = "7"
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}