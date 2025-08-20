package com.cocido.formokaizen.domain.repository

import com.cocido.formokaizen.domain.entities.TarjetaRoja
import com.cocido.formokaizen.utils.Resource
import kotlinx.coroutines.flow.Flow

interface TarjetasRepository {
    suspend fun getTarjetas(
        page: Int = 1,
        perPage: Int = 20,
        status: String? = null,
        category: Int? = null,
        priority: String? = null,
        assignedTo: String? = null,
        createdBy: String? = null,
        search: String? = null
    ): Flow<Resource<List<TarjetaRoja>>>
    
    // Métodos adicionales para el ViewModel
    suspend fun getAllTarjetas(): Flow<Resource<List<TarjetaRoja>>>
    suspend fun getMyTarjetas(): Flow<Resource<List<TarjetaRoja>>>
    suspend fun getTarjetasByStatus(status: String): Flow<Resource<List<TarjetaRoja>>>
    suspend fun getTarjetasByCategory(categoryId: Int): Flow<Resource<List<TarjetaRoja>>>
    suspend fun searchTarjetas(query: String): Flow<Resource<List<TarjetaRoja>>>
    
    suspend fun getTarjetaById(id: Int): Flow<Resource<TarjetaRoja>>
    suspend fun createTarjeta(tarjeta: TarjetaRoja): Flow<Resource<TarjetaRoja>>
    suspend fun createTarjeta(request: com.cocido.formokaizen.domain.entities.CreateTarjetaRequest): Flow<Resource<TarjetaRoja>>
    suspend fun updateTarjeta(tarjeta: TarjetaRoja): Flow<Resource<TarjetaRoja>>
    suspend fun deleteTarjeta(id: Int): Flow<Resource<Unit>>
    suspend fun approveTarjeta(id: Int, action: String, comment: String?): Flow<Resource<TarjetaRoja>>
    suspend fun rejectTarjeta(id: Int, comment: String?): Flow<Resource<TarjetaRoja>>
    suspend fun markAsInProgress(id: Int): Flow<Resource<TarjetaRoja>>
    suspend fun markAsResolved(id: Int, resolution: String): Flow<Resource<TarjetaRoja>>
    suspend fun addComment(tarjetaId: Int, comment: String, isInternal: Boolean): Flow<Resource<Unit>>
    suspend fun uploadImage(tarjetaId: Int, imagePath: String, description: String): Flow<Resource<Unit>>
    suspend fun getDashboardStats(): Flow<Resource<Map<String, Any>>>
    
    // Offline support
    fun getTarjetasOffline(): Flow<List<TarjetaRoja>>
    suspend fun syncTarjetas(): Flow<Resource<Unit>>
}