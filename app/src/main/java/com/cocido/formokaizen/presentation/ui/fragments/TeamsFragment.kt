package com.cocido.formokaizen.presentation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.cocido.formokaizen.databinding.FragmentTeamsBinding
import com.cocido.formokaizen.presentation.viewmodels.AuthViewModel
import com.cocido.formokaizen.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TeamsFragment : Fragment() {
    
    private var _binding: FragmentTeamsBinding? = null
    private val binding get() = _binding!!
    
    private val authViewModel: AuthViewModel by viewModels()
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTeamsBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupClickListeners()
        loadTeamInfo()
        observeViewModel()
    }
    
    private fun setupClickListeners() {
        binding.btnViewMembers.setOnClickListener {
            Toast.makeText(context, "Mostrando miembros del equipo", Toast.LENGTH_SHORT).show()
        }
        
        binding.btnTeamStats.setOnClickListener {
            Toast.makeText(context, "Estadísticas del equipo en desarrollo", Toast.LENGTH_SHORT).show()
        }
        
        binding.btnInviteMembers.setOnClickListener {
            Toast.makeText(context, "Invitar miembros en desarrollo", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun loadTeamInfo() {
        authViewModel.getCurrentUser()
    }
    
    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            authViewModel.currentUser.collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        resource.data?.let { user ->
                            updateTeamInfo(user.department ?: "Equipo General")
                        }
                    }
                    is Resource.Error -> {
                        updateTeamInfo("Equipo General")
                    }
                    else -> {}
                }
            }
        }
    }
    
    private fun updateTeamInfo(teamName: String) {
        binding.apply {
            tvTeamName.text = teamName
            tvTeamDescription.text = "Equipo colaborativo de mejora continua en $teamName"
            tvMemberCount.text = "5"
            tvActiveTarjetas.text = "3"
            tvResolvedTarjetas.text = "12"
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}