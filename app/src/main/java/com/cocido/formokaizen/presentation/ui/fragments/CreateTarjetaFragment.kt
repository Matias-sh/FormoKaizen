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
    
    private var selectedCategory: Category? = null
    private var categories: List<Category> = emptyList()
    
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
        setupCategoryDropdown()
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
    
    private fun setupCategoryDropdown() {
        // Observe categories from ViewModel
        viewLifecycleOwner.lifecycleScope.launch {
            tarjetasViewModel.categories.collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        categories = resource.data ?: emptyList()
                        setupCategoryAdapter()
                    }
                    is Resource.Error -> {
                        Toast.makeText(context, "Error al cargar categorías", Toast.LENGTH_SHORT).show()
                    }
                    else -> {}
                }
            }
        }
    }
    
    private fun setupCategoryAdapter() {
        val categoryNames = categories.map { it.name }
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            categoryNames
        )
        
        binding.actvCategory.setAdapter(adapter)
        binding.actvCategory.setOnItemClickListener { _, _, position, _ ->
            selectedCategory = categories[position]
        }
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
        val title = binding.etTitle.text.toString().trim()
        val description = binding.etDescription.text.toString().trim()
        val sector = binding.etSector.text.toString().trim()
        val rootCause = binding.etRootCause.text.toString().trim()
        
        // Clear previous errors
        clearErrors()
        
        // Validate required fields
        var isValid = true
        
        if (title.isEmpty()) {
            binding.tilTitle.error = "El título es requerido"
            isValid = false
        }
        
        if (description.isEmpty()) {
            binding.tilDescription.error = "La descripción es requerida"
            isValid = false
        }
        
        if (selectedCategory == null) {
            binding.tilCategory.error = "Seleccione una categoría"
            isValid = false
        }
        
        if (sector.isEmpty()) {
            binding.tilSector.error = "El sector es requerido"
            isValid = false
        }
        
        if (!isValid) {
            return
        }
        
        // Get selected priority
        val priority = getSelectedPriority()
        
        // Create tarjeta request
        val request = CreateTarjetaRequest(
            title = title,
            description = description,
            categoryId = selectedCategory!!.id,
            workAreaId = null, // No tenemos work area seleccionada por ahora
            priority = priority,
            sector = sector,
            motivo = description, // Usar description como motivo por ahora
            destinoFinal = "TBD", // To be determined
            estimatedResolutionDate = null,
            assignedToId = null,
            imageUris = selectedPhotos.map { it.toString() }
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
        binding.tilTitle.error = null
        binding.tilDescription.error = null
        binding.tilCategory.error = null
        binding.tilSector.error = null
        binding.tilRootCause.error = null
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
        binding.etTitle.isEnabled = !show
        binding.etDescription.isEnabled = !show
        binding.actvCategory.isEnabled = !show
        binding.etSector.isEnabled = !show
        binding.etRootCause.isEnabled = !show
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