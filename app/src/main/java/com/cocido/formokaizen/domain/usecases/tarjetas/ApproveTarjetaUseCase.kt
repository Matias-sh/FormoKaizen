package com.cocido.formokaizen.domain.usecases.tarjetas

import com.cocido.formokaizen.domain.entities.TarjetaRoja
import com.cocido.formokaizen.domain.entities.User
import com.cocido.formokaizen.domain.repository.TarjetasRepository
import com.cocido.formokaizen.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class ApproveTarjetaUseCase @Inject constructor(
    private val tarjetasRepository: TarjetasRepository
) {
    suspend operator fun invoke(
        tarjetaId: Int,
        action: String, // "approve" or "reject"
        currentUser: User,
        comment: String? = null
    ): Flow<Resource<TarjetaRoja>> {
        // Validaciones
        if (!currentUser.canApprove()) {
            return flowOf(Resource.Error("No tienes permisos para aprobar tarjetas"))
        }
        
        if (action !in listOf("approve", "reject")) {
            return flowOf(Resource.Error("Acción inválida"))
        }
        
        if (action == "reject" && comment.isNullOrBlank()) {
            return flowOf(Resource.Error("Es necesario agregar un comentario al rechazar una tarjeta"))
        }
        
        return tarjetasRepository.approveTarjeta(tarjetaId, action, comment)
    }
}