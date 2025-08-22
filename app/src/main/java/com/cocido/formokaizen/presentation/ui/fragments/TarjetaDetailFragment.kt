package com.cocido.formokaizen.presentation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.cocido.formokaizen.R
import com.cocido.formokaizen.databinding.FragmentTarjetaDetailBinding
import com.cocido.formokaizen.domain.entities.TarjetaRoja
import com.cocido.formokaizen.domain.entities.TarjetaStatus
import com.cocido.formokaizen.presentation.viewmodel.TarjetasViewModel
import com.cocido.formokaizen.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TarjetaDetailFragment : Fragment() {
    
    private var _binding: FragmentTarjetaDetailBinding? = null
    private val binding get() = _binding!!
    
    private val args: TarjetaDetailFragmentArgs by navArgs()
    private val tarjetasViewModel: TarjetasViewModel by viewModels()
    
    private var currentTarjeta: TarjetaRoja? = null
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTarjetaDetailBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupClickListeners()
        observeViewModel()
        
        // Load tarjeta details using tarjetaId from args
        val tarjetaId = args.tarjetaId
        tarjetasViewModel.getTarjetaDetail(tarjetaId)
    }
    
    private fun setupClickListeners() {
        // Los botones se configurarán cuando existan en el layout
        // binding.btnRetry.setOnClickListener {
        //     tarjetasViewModel.getTarjetaDetail(args.tarjetaId)
        // }
    }
    
    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            tarjetasViewModel.tarjetaDetail.collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        showLoading(true)
                        showError(false)
                    }
                    is Resource.Success -> {
                        showLoading(false)
                        showError(false)
                        resource.data?.let { tarjeta ->
                            currentTarjeta = tarjeta
                            updateTarjetaDetail(tarjeta)
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
        
        // Observar actualizaciones de tarjetas
        viewLifecycleOwner.lifecycleScope.launch {
            tarjetasViewModel.updateTarjetaState.collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        // Deshabilitar botones de acción
                        setActionButtonsEnabled(false)
                    }
                    is Resource.Success -> {
                        setActionButtonsEnabled(true)
                        Toast.makeText(context, "Tarjeta actualizada exitosamente", Toast.LENGTH_SHORT).show()
                        // Recargar detalles
                        tarjetasViewModel.getTarjetaDetail(args.tarjetaId)
                    }
                    is Resource.Error -> {
                        setActionButtonsEnabled(true)
                        Toast.makeText(
                            context,
                            resource.message ?: "Error al actualizar tarjeta",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    else -> {
                        setActionButtonsEnabled(true)
                    }
                }
            }
        }
    }
    
    private fun updateTarjetaDetail(tarjeta: TarjetaRoja) {
        // Por ahora, mostrar información básica con Toast
        // Cuando el layout esté implementado, se podrá mostrar la información completa
        Toast.makeText(
            context,
            "Tarjeta: ${tarjeta.numero} - ${tarjeta.descripcion}",
            Toast.LENGTH_LONG
        ).show()
    }
    
    private fun setupActionButtons(tarjeta: TarjetaRoja) {
        // Por ahora simplificado hasta que se implemente el layout completo
    }
    
    private fun setActionButtonsEnabled(enabled: Boolean) {
        // Por ahora simplificado hasta que se implemente el layout completo
    }
    
    private fun showLoading(show: Boolean) {
        // Por ahora simplificado hasta que se implemente el layout completo
    }
    
    private fun showError(show: Boolean, message: String? = null) {
        // Por ahora simplificado hasta que se implemente el layout completo
        if (show && message != null) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}