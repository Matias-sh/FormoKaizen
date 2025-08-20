package com.cocido.formokaizen.domain.usecases.tarjetas

import com.cocido.formokaizen.domain.entities.TarjetaRoja
import com.cocido.formokaizen.domain.repository.TarjetasRepository
import com.cocido.formokaizen.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTarjetasUseCase @Inject constructor(
    private val tarjetasRepository: TarjetasRepository
) {
    suspend operator fun invoke(
        page: Int = 1,
        perPage: Int = 20,
        status: String? = null,
        category: Int? = null,
        priority: String? = null,
        assignedTo: String? = null,
        createdBy: String? = null,
        search: String? = null
    ): Flow<Resource<List<TarjetaRoja>>> {
        return tarjetasRepository.getTarjetas(
            page = page,
            perPage = perPage,
            status = status,
            category = category,
            priority = priority,
            assignedTo = assignedTo,
            createdBy = createdBy,
            search = search
        )
    }
}