package com.cocido.formokaizen.domain.usecases.tarjetas

import com.cocido.formokaizen.domain.entities.TarjetaRoja
import com.cocido.formokaizen.domain.repository.TarjetasRepository
import com.cocido.formokaizen.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class GetTarjetaByIdUseCase @Inject constructor(
    private val tarjetasRepository: TarjetasRepository
) {
    suspend operator fun invoke(id: Int): Flow<Resource<TarjetaRoja>> {
        if (id <= 0) {
            return flowOf(Resource.Error("ID de tarjeta inválido"))
        }
        
        return tarjetasRepository.getTarjetaById(id)
    }
}