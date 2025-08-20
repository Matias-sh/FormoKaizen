package com.cocido.formokaizen.presentation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.cocido.formokaizen.R
import com.cocido.formokaizen.databinding.FragmentTarjetasBinding
import com.cocido.formokaizen.presentation.ui.adapters.TarjetasAdapter
import com.cocido.formokaizen.presentation.viewmodel.TarjetasViewModel
import com.cocido.formokaizen.utils.Resource
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TarjetasFragment : Fragment() {
    
    private var _binding: FragmentTarjetasBinding? = null
    private val binding get() = _binding!!
    
    private val tarjetasViewModel: TarjetasViewModel by viewModels()
    private lateinit var tarjetasAdapter: TarjetasAdapter
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTarjetasBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupRecyclerView()
        setupClickListeners()
        setupSearch()
        setupFilters()
        observeViewModel()
    }
    
    private fun setupRecyclerView() {
        tarjetasAdapter = TarjetasAdapter { tarjeta ->
            // Navigate to tarjeta detail
            val action = TarjetasFragmentDirections.actionTarjetasToDetail(tarjeta.id)
            findNavController().navigate(action)
        }
        
        binding.rvTarjetas.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = tarjetasAdapter
        }
    }
    
    private fun setupClickListeners() {
        binding.fabNewTarjeta.setOnClickListener {
            findNavController().navigate(R.id.action_tarjetas_to_create)
        }
        
        binding.btnCreateFirst.setOnClickListener {
            findNavController().navigate(R.id.action_tarjetas_to_create)
        }
        
        binding.btnRetry.setOnClickListener {
            tarjetasViewModel.loadTarjetas()
        }
    }
    
    private fun setupSearch() {
        binding.etSearch.doAfterTextChanged { text ->
            val query = text?.toString()?.trim() ?: ""
            if (query.length >= 2) {
                tarjetasViewModel.searchTarjetas(query)
            } else if (query.isEmpty()) {
                // Reset to current filter
                applyCurrentFilter()
            }
        }
    }
    
    private fun setupFilters() {
        binding.chipGroupFilters.setOnCheckedStateChangeListener { group, checkedIds ->
            if (checkedIds.isNotEmpty()) {
                val checkedChip = group.findViewById<Chip>(checkedIds[0])
                when (checkedChip.id) {
                    R.id.chipAll -> tarjetasViewModel.loadTarjetas()
                    R.id.chipMine -> tarjetasViewModel.loadMyTarjetas()
                    R.id.chipOpen -> tarjetasViewModel.filterTarjetasByStatus("OPEN")
                    R.id.chipInProgress -> tarjetasViewModel.filterTarjetasByStatus("IN_PROGRESS")
                    R.id.chipResolved -> tarjetasViewModel.filterTarjetasByStatus("RESOLVED")
                }
            }
        }
    }
    
    private fun applyCurrentFilter() {
        val checkedChipId = binding.chipGroupFilters.checkedChipId
        when (checkedChipId) {
            R.id.chipAll -> tarjetasViewModel.loadTarjetas()
            R.id.chipMine -> tarjetasViewModel.loadMyTarjetas()
            R.id.chipOpen -> tarjetasViewModel.filterTarjetasByStatus("OPEN")
            R.id.chipInProgress -> tarjetasViewModel.filterTarjetasByStatus("IN_PROGRESS")
            R.id.chipResolved -> tarjetasViewModel.filterTarjetasByStatus("RESOLVED")
            else -> tarjetasViewModel.loadTarjetas()
        }
    }
    
    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            tarjetasViewModel.tarjetas.collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        showLoading(true)
                        showError(false)
                        showEmptyState(false)
                    }
                    is Resource.Success -> {
                        showLoading(false)
                        showError(false)
                        
                        val tarjetas = resource.data ?: emptyList()
                        if (tarjetas.isEmpty()) {
                            showEmptyState(true)
                        } else {
                            showEmptyState(false)
                            tarjetasAdapter.submitList(tarjetas)
                        }
                    }
                    is Resource.Error -> {
                        showLoading(false)
                        showError(true, resource.message)
                        showEmptyState(false)
                        
                        Toast.makeText(
                            context,
                            resource.message ?: "Error al cargar tarjetas",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    is Resource.Idle -> {
                        showLoading(false)
                        showError(false)
                        showEmptyState(false)
                    }
                }
            }
        }
    }
    
    private fun showLoading(show: Boolean) {
        binding.llLoading.visibility = if (show) View.VISIBLE else View.GONE
        binding.rvTarjetas.visibility = if (show) View.GONE else View.VISIBLE
    }
    
    private fun showError(show: Boolean, message: String? = null) {
        binding.llErrorState.visibility = if (show) View.VISIBLE else View.GONE
        if (show && message != null) {
            binding.tvErrorMessage.text = message
        }
        binding.rvTarjetas.visibility = if (show) View.GONE else View.VISIBLE
    }
    
    private fun showEmptyState(show: Boolean) {
        binding.llEmptyState.visibility = if (show) View.VISIBLE else View.GONE
        binding.rvTarjetas.visibility = if (show) View.GONE else View.VISIBLE
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}