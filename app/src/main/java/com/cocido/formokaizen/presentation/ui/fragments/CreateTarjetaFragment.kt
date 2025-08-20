package com.cocido.formokaizen.presentation.ui.fragments

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.cocido.formokaizen.R
import com.cocido.formokaizen.databinding.FragmentCreateTarjetaBinding
import com.cocido.formokaizen.domain.entities.*
import com.cocido.formokaizen.presentation.viewmodel.TarjetasViewModel
import com.cocido.formokaizen.utils.Resource
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class CreateTarjetaFragment : Fragment() {
    
    private var _binding: FragmentCreateTarjetaBinding? = null
    private val binding get() = _binding!!
    
    private val tarjetasViewModel: TarjetasViewModel by viewModels()
    
    private var currentPhotoPath: String = ""
    private val selectedPhotos = mutableListOf<String>()
    
    // Camera permission launcher
    private val requestCameraPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            dispatchTakePictureIntent()
        } else {
            Toast.makeText(context, getString(R.string.camera_permission_required), Toast.LENGTH_SHORT).show()
        }
    }
    
    // Camera launcher
    private val takePictureLauncher = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            selectedPhotos.add(currentPhotoPath)
            updatePhotoPreview()
        }
    }
    
    // Gallery launcher
    private val selectImageLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            selectedPhotos.add(it.toString())
            updatePhotoPreview()
        }
    }
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateTarjetaBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupClickListeners()
        setupDatePickers()
        setupValidation()
        observeViewModel()
    }
    
    private fun setupClickListeners() {
        binding.btnSave.setOnClickListener {
            validateAndCreateTarjeta()
        }
        
        binding.btnCancel.setOnClickListener {
            findNavController().popBackStack()
        }
        
        binding.btnTakePhoto.setOnClickListener {
            checkCameraPermissionAndTakePhoto()
        }
        
        binding.btnSelectPhoto.setOnClickListener {
            selectImageLauncher.launch("image/*")
        }
    }
    
    private fun setupDatePickers() {
        // Set current date as default for fecha
        val currentDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
        binding.etFecha.setText(currentDate)
        
        // Set up date picker for fecha field
        binding.etFecha.setOnClickListener {
            showDatePicker { date ->
                binding.etFecha.setText(date)
            }
        }
        
        // Set up date picker for fechaFinal field
        binding.etFechaFinal.setOnClickListener {
            showDatePicker { date ->
                binding.etFechaFinal.setText(date)
            }
        }
    }
    
    private fun showDatePicker(onDateSelected: (String) -> Unit) {
        val calendar = java.util.Calendar.getInstance()
        val year = calendar.get(java.util.Calendar.YEAR)
        val month = calendar.get(java.util.Calendar.MONTH)
        val day = calendar.get(java.util.Calendar.DAY_OF_MONTH)
        
        val datePickerDialog = android.app.DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate = String.format("%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear)
                onDateSelected(formattedDate)
            },
            year, month, day
        )
        datePickerDialog.show()
    }
    
    private fun setupValidation() {
        // Real-time validation for numero field
        binding.etNumero.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val numero = binding.etNumero.text.toString().trim()
                if (numero.isNotEmpty()) {
                    validateNumeroUniqueness(numero)
                }
            }
        }
        
        // Auto-generate numero if empty on focus
        binding.etNumero.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && binding.etNumero.text.toString().isEmpty()) {
                generateUniqueNumero()
            }
        }
    }
    
    private fun validateNumeroUniqueness(numero: String) {
        tarjetasViewModel.validateNumero(numero)
    }
    
    private fun generateUniqueNumero() {
        // Generate a simple number based on timestamp
        val timestamp = System.currentTimeMillis()
        val numero = "TR${timestamp.toString().takeLast(6)}"
        binding.etNumero.setText(numero)
    }
    
    private fun checkCameraPermissionAndTakePhoto() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                dispatchTakePictureIntent()
            }
            else -> {
                requestCameraPermission.launch(Manifest.permission.CAMERA)
            }
        }
    }
    
    private fun dispatchTakePictureIntent() {
        val photoFile: File? = try {
            createImageFile()
        } catch (ex: IOException) {
            null
        }
        
        photoFile?.also {
            val photoURI: Uri = FileProvider.getUriForFile(
                requireContext(),
                "${requireContext().packageName}.fileprovider",
                it
            )
            takePictureLauncher.launch(photoURI)
        }
    }
    
    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? = requireContext().getExternalFilesDir(android.os.Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            currentPhotoPath = absolutePath
        }
    }
    
    private fun updatePhotoPreview() {
        if (selectedPhotos.isNotEmpty()) {
            binding.rvPhotoPreview.visibility = View.VISIBLE
            // TODO: Set up photo preview adapter
        } else {
            binding.rvPhotoPreview.visibility = View.GONE
        }
    }
    
    private fun validateAndCreateTarjeta() {
        val numero = binding.etNumero.text.toString().trim()
        val fecha = binding.etFecha.text.toString().trim()
        val sector = binding.etSector.text.toString().trim()
        val descripcion = binding.etDescripcion.text.toString().trim()
        val motivo = binding.etMotivo.text.toString().trim()
        val quienLoHizo = binding.etQuienLoHizo.text.toString().trim()
        val destinoFinal = binding.etDestinoFinal.text.toString().trim()
        val fechaFinal = binding.etFechaFinal.text.toString().trim()
        
        // Clear previous errors
        clearErrors()
        
        // Validate required fields
        var isValid = true
        
        if (numero.isEmpty()) {
            binding.tilNumero.error = "El número es requerido"
            isValid = false
        }
        
        if (fecha.isEmpty()) {
            binding.tilFecha.error = "La fecha es requerida"
            isValid = false
        }
        
        if (sector.isEmpty()) {
            binding.tilSector.error = "El sector es requerido"
            isValid = false
        }
        
        if (descripcion.isEmpty()) {
            binding.tilDescripcion.error = "La descripción es requerida"
            isValid = false
        }
        
        if (motivo.isEmpty()) {
            binding.tilMotivo.error = "El motivo es requerido"
            isValid = false
        }
        
        if (quienLoHizo.isEmpty()) {
            binding.tilQuienLoHizo.error = "Este campo es requerido"
            isValid = false
        }
        
        if (destinoFinal.isEmpty()) {
            binding.tilDestinoFinal.error = "El destino final es requerido"
            isValid = false
        }
        
        if (!isValid) {
            return
        }
        
        // Get selected priority
        val priority = getSelectedPriority()
        
        // Create tarjeta request
        val request = CreateTarjetaRequest(
            numero = numero,
            fecha = fecha,
            sector = sector,
            descripcion = descripcion,
            motivo = motivo,
            quienLoHizo = quienLoHizo,
            destinoFinal = destinoFinal,
            fechaFinal = if (fechaFinal.isEmpty()) null else fechaFinal,
            priority = priority,
            assignedToId = null,
            imageUris = selectedPhotos
        )
        
        // Create tarjeta
        tarjetasViewModel.createTarjeta(request)
    }
    
    private fun getSelectedPriority(): Priority {
        return when (binding.chipGroupPriority.checkedChipId) {
            R.id.chipPriorityLow -> Priority.LOW
            R.id.chipPriorityMedium -> Priority.MEDIUM
            R.id.chipPriorityHigh -> Priority.HIGH
            R.id.chipPriorityCritical -> Priority.CRITICAL
            else -> Priority.MEDIUM
        }
    }
    
    private fun clearErrors() {
        binding.tilNumero.error = null
        binding.tilFecha.error = null
        binding.tilSector.error = null
        binding.tilDescripcion.error = null
        binding.tilMotivo.error = null
        binding.tilQuienLoHizo.error = null
        binding.tilDestinoFinal.error = null
        binding.tilFechaFinal.error = null
    }
    
    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            tarjetasViewModel.createTarjetaState.collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        showLoading(true)
                    }
                    is Resource.Success -> {
                        showLoading(false)
                        Toast.makeText(context, "Tarjeta creada exitosamente", Toast.LENGTH_SHORT).show()
                        
                        // Reset form state
                        tarjetasViewModel.resetCreateTarjetaState()
                        
                        // Navigate back
                        findNavController().popBackStack()
                    }
                    is Resource.Error -> {
                        showLoading(false)
                        Toast.makeText(
                            context,
                            resource.message ?: "Error al crear la tarjeta",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    is Resource.Idle -> {
                        showLoading(false)
                    }
                }
            }
        }
    }
    
    private fun showLoading(show: Boolean) {
        binding.llLoading.visibility = if (show) View.VISIBLE else View.GONE
        binding.btnSave.isEnabled = !show
        binding.btnCancel.isEnabled = !show
        
        // Disable form inputs
        binding.etNumero.isEnabled = !show
        binding.etFecha.isEnabled = !show
        binding.etSector.isEnabled = !show
        binding.etDescripcion.isEnabled = !show
        binding.etMotivo.isEnabled = !show
        binding.etQuienLoHizo.isEnabled = !show
        binding.etDestinoFinal.isEnabled = !show
        binding.etFechaFinal.isEnabled = !show
        binding.btnTakePhoto.isEnabled = !show
        binding.btnSelectPhoto.isEnabled = !show
        
        // Disable priority chips
        for (i in 0 until binding.chipGroupPriority.childCount) {
            val chip = binding.chipGroupPriority.getChildAt(i) as Chip
            chip.isEnabled = !show
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}