package com.cocido.formokaizen.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cocido.formokaizen.domain.entities.CreateTarjetaRequest
import com.cocido.formokaizen.domain.entities.TarjetaRoja
import com.cocido.formokaizen.domain.repository.TarjetasRepository
import com.cocido.formokaizen.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TarjetasViewModel @Inject constructor(
    private val tarjetasRepository: TarjetasRepository
) : ViewModel() {
    
    private val _tarjetas = MutableStateFlow<Resource<List<TarjetaRoja>>>(Resource.Idle())
    val tarjetas: StateFlow<Resource<List<TarjetaRoja>>> = _tarjetas.asStateFlow()
    
    private val _numeroValidation = MutableStateFlow<Resource<Boolean>>(Resource.Idle())
    val numeroValidation: StateFlow<Resource<Boolean>> = _numeroValidation.asStateFlow()
    
    private val _createTarjetaState = MutableStateFlow<Resource<TarjetaRoja>>(Resource.Idle())
    val createTarjetaState: StateFlow<Resource<TarjetaRoja>> = _createTarjetaState.asStateFlow()
    
    private val _tarjetaDetail = MutableStateFlow<Resource<TarjetaRoja>>(Resource.Idle())
    val tarjetaDetail: StateFlow<Resource<TarjetaRoja>> = _tarjetaDetail.asStateFlow()
    
    private val _updateTarjetaState = MutableStateFlow<Resource<TarjetaRoja>>(Resource.Idle())
    val updateTarjetaState: StateFlow<Resource<TarjetaRoja>> = _updateTarjetaState.asStateFlow()
    
    init {
        loadTarjetas()
    }
    
    fun loadTarjetas() {
        viewModelScope.launch {
            tarjetasRepository.getAllTarjetas().collect { resource ->
                _tarjetas.value = resource
            }
        }
    }
    
    fun loadMyTarjetas() {
        viewModelScope.launch {
            tarjetasRepository.getMyTarjetas().collect { resource ->
                _tarjetas.value = resource
            }
        }
    }
    
    fun validateNumero(numero: String, excludeId: Int? = null) {
        viewModelScope.launch {
            tarjetasRepository.validateNumero(numero, excludeId).collect { resource ->
                _numeroValidation.value = resource
            }
        }
    }
    
    fun createTarjeta(request: CreateTarjetaRequest) {
        viewModelScope.launch {
            tarjetasRepository.createTarjeta(request).collect { resource ->
                _createTarjetaState.value = resource
                if (resource is Resource.Success) {
                    // Reload tarjetas after successful creation
                    loadTarjetas()
                }
            }
        }
    }
    
    fun getTarjetaDetail(id: Int) {
        viewModelScope.launch {
            tarjetasRepository.getTarjetaById(id).collect { resource ->
                _tarjetaDetail.value = resource
            }
        }
    }
    
    fun updateTarjeta(tarjeta: TarjetaRoja) {
        viewModelScope.launch {
            tarjetasRepository.updateTarjeta(tarjeta).collect { resource ->
                _updateTarjetaState.value = resource
                if (resource is Resource.Success) {
                    // Update the detail if it's the same tarjeta
                    if (_tarjetaDetail.value is Resource.Success && 
                        (_tarjetaDetail.value as Resource.Success).data?.id == tarjeta.id) {
                        _tarjetaDetail.value = Resource.Success(resource.data!!)
                    }
                    // Reload tarjetas list
                    loadTarjetas()
                }
            }
        }
    }
    
    fun approveTarjeta(id: Int, action: String = "APPROVED", comment: String? = null) {
        viewModelScope.launch {
            tarjetasRepository.approveTarjeta(id, action, comment).collect { resource ->
                _updateTarjetaState.value = resource
                if (resource is Resource.Success) {
                    getTarjetaDetail(id)
                    loadTarjetas()
                }
            }
        }
    }
    
    fun rejectTarjeta(id: Int, reason: String) {
        viewModelScope.launch {
            tarjetasRepository.rejectTarjeta(id, reason).collect { resource ->
                _updateTarjetaState.value = resource
                if (resource is Resource.Success) {
                    getTarjetaDetail(id)
                    loadTarjetas()
                }
            }
        }
    }
    
    fun markAsInProgress(id: Int) {
        viewModelScope.launch {
            tarjetasRepository.markAsInProgress(id).collect { resource ->
                _updateTarjetaState.value = resource
                if (resource is Resource.Success) {
                    getTarjetaDetail(id)
                    loadTarjetas()
                }
            }
        }
    }
    
    fun markAsResolved(id: Int, solution: String) {
        viewModelScope.launch {
            tarjetasRepository.markAsResolved(id, solution).collect { resource ->
                _updateTarjetaState.value = resource
                if (resource is Resource.Success) {
                    getTarjetaDetail(id)
                    loadTarjetas()
                }
            }
        }
    }
    
    fun filterTarjetasByStatus(status: String) {
        viewModelScope.launch {
            tarjetasRepository.getTarjetasByStatus(status).collect { resource ->
                _tarjetas.value = resource
            }
        }
    }
    
    fun filterTarjetasBySector(sector: String) {
        viewModelScope.launch {
            tarjetasRepository.getTarjetasBySector(sector).collect { resource ->
                _tarjetas.value = resource
            }
        }
    }
    
    fun searchTarjetas(query: String) {
        viewModelScope.launch {
            tarjetasRepository.searchTarjetas(query).collect { resource ->
                _tarjetas.value = resource
            }
        }
    }
    
    fun resetCreateTarjetaState() {
        _createTarjetaState.value = Resource.Idle()
    }
    
    fun resetUpdateTarjetaState() {
        _updateTarjetaState.value = Resource.Idle()
    }
    
    fun resetNumeroValidation() {
        _numeroValidation.value = Resource.Idle()
    }
}