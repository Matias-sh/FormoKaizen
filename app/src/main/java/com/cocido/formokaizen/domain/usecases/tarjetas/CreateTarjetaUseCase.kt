package com.cocido.formokaizen.domain.usecases.tarjetas

import com.cocido.formokaizen.domain.entities.TarjetaRoja
import com.cocido.formokaizen.domain.repository.TarjetasRepository
import com.cocido.formokaizen.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class CreateTarjetaUseCase @Inject constructor(
    private val tarjetasRepository: TarjetasRepository
) {
    suspend operator fun invoke(tarjeta: TarjetaRoja): Flow<Resource<TarjetaRoja>> {
        // Validaciones
        if (tarjeta.title.isBlank()) {
            return flowOf(Resource.Error("El título es requerido"))
        }
        
        if (tarjeta.title.length < 5) {
            return flowOf(Resource.Error("El título debe tener al menos 5 caracteres"))
        }
        
        if (tarjeta.description.isBlank()) {
            return flowOf(Resource.Error("La descripción es requerida"))
        }
        
        if (tarjeta.description.length < 10) {
            return flowOf(Resource.Error("La descripción debe tener al menos 10 caracteres"))
        }
        
        if (tarjeta.motivo.isBlank()) {
            return flowOf(Resource.Error("El motivo/causa raíz es requerido"))
        }
        
        if (tarjeta.destinoFinal.isBlank()) {
            return flowOf(Resource.Error("La acción correctiva es requerida"))
        }
        
        return tarjetasRepository.createTarjeta(tarjeta)
    }
}