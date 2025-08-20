package com.cocido.formokaizen.presentation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.cocido.formokaizen.databinding.FragmentTarjetaDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TarjetaDetailFragment : Fragment() {
    
    private var _binding: FragmentTarjetaDetailBinding? = null
    private val binding get() = _binding!!
    
    private val args: TarjetaDetailFragmentArgs by navArgs()
    
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
        
        // Get tarjetaId from args
        val tarjetaId = args.tarjetaId
        // TODO: Load tarjeta details using tarjetaId
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}