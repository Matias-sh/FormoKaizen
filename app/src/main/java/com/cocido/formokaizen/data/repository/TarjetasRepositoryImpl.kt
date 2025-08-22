package com.cocido.formokaizen.data.repository

import android.util.Log
import com.cocido.formokaizen.data.remote.api.TarjetasApi
import com.cocido.formokaizen.data.remote.dto.toDomainModel
import com.cocido.formokaizen.domain.entities.*
import com.cocido.formokaizen.domain.repository.TarjetasRepository
import com.cocido.formokaizen.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class TarjetasRepositoryImpl @Inject constructor(
    private val tarjetasApi: TarjetasApi
    // private val tarjetasDao: TarjetasDao
) : TarjetasRepository {

    override suspend fun getTarjetas(
        page: Int,
        perPage: Int,
        status: String?,
        category: Int?,
        priority: String?,
        assignedTo: String?,
        createdBy: String?,
        search: String?
    ): Flow<Resource<List<TarjetaRoja>>> {
        return flowOf(Resource.Success(emptyList()))
    }

    override suspend fun getAllTarjetas(): Flow<Resource<List<TarjetaRoja>>> = flow {
        emit(Resource.Loading())
        
        try {
            Log.d("TarjetasRepository", "Llamando a tarjetasApi.getTarjetas()")
            val response = tarjetasApi.getTarjetas()
            
            if (response.isSuccessful && response.body() != null) {
                val paginatedResponse = response.body()!!
                val tarjetas = paginatedResponse.results.map { it.toDomainModel() }
                
                Log.d("TarjetasRepository", "Tarjetas obtenidas exitosamente: ${tarjetas.size} tarjetas")
                emit(Resource.Success(tarjetas))
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Error al obtener tarjetas"
                Log.e("TarjetasRepository", "Error API: $errorMessage")
                emit(Resource.Error(errorMessage))
            }
        } catch (e: Exception) {
            Log.e("TarjetasRepository", "Error de conexión: ${e.localizedMessage}", e)
            emit(Resource.Error(e.localizedMessage ?: "Error de conexión"))
        }
    }

    override suspend fun getMyTarjetas(): Flow<Resource<List<TarjetaRoja>>> = flow {
        emit(Resource.Loading())
        
        try {
            Log.d("TarjetasRepository", "Obteniendo mis tarjetas")
            
            // Usar el endpoint para obtener tarjetas del usuario actual
            val response = tarjetasApi.getMyTarjetas()
            
            if (response.isSuccessful && response.body() != null) {
                val paginatedResponse = response.body()!!
                val tarjetas = paginatedResponse.results.map { it.toDomainModel() }
                
                Log.d("TarjetasRepository", "Mis tarjetas obtenidas exitosamente: ${tarjetas.size} tarjetas")
                emit(Resource.Success(tarjetas))
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Error al obtener mis tarjetas"
                Log.e("TarjetasRepository", "Error API: $errorMessage")
                emit(Resource.Error(errorMessage))
            }
        } catch (e: Exception) {
            Log.e("TarjetasRepository", "Error de conexión: ${e.localizedMessage}", e)
            emit(Resource.Error(e.localizedMessage ?: "Error de conexión"))
        }
    }

    override suspend fun getTarjetasByStatus(status: String): Flow<Resource<List<TarjetaRoja>>> {
        return flowOf(Resource.Success(emptyList()))
    }

    override suspend fun getTarjetasBySector(sector: String): Flow<Resource<List<TarjetaRoja>>> {
        return flowOf(Resource.Success(emptyList()))
    }

    override suspend fun searchTarjetas(query: String): Flow<Resource<List<TarjetaRoja>>> {
        return flowOf(Resource.Success(emptyList()))
    }

    override suspend fun getTarjetaById(id: Int): Flow<Resource<TarjetaRoja>> = flow {
        emit(Resource.Loading())
        
        try {
            Log.d("TarjetasRepository", "Obteniendo tarjeta por ID: $id")
            
            val response = tarjetasApi.getTarjetaById(id)
            
            if (response.isSuccessful && response.body() != null) {
                val tarjetaDto = response.body()!!
                val tarjeta = tarjetaDto.toDomainModel()
                
                Log.d("TarjetasRepository", "Tarjeta obtenida exitosamente: ${tarjeta.numero}")
                emit(Resource.Success(tarjeta))
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Error al obtener la tarjeta"
                Log.e("TarjetasRepository", "Error API: $errorMessage")
                emit(Resource.Error(errorMessage))
            }
        } catch (e: Exception) {
            Log.e("TarjetasRepository", "Error de conexión: ${e.localizedMessage}", e)
            emit(Resource.Error(e.localizedMessage ?: "Error de conexión"))
        }
    }

    override suspend fun createTarjeta(tarjeta: TarjetaRoja): Flow<Resource<TarjetaRoja>> = flow {
        emit(Resource.Loading())
        
        try {
            Log.d("TarjetasRepository", "Creando tarjeta (método TarjetaRoja): ${tarjeta.numero}")
            
            val createRequest = com.cocido.formokaizen.data.remote.dto.CreateTarjetaRequestDto(
                numero = tarjeta.numero,
                fecha = tarjeta.fecha,
                sector = tarjeta.sector,
                descripcion = tarjeta.descripcion,
                motivo = tarjeta.motivo,
                quienLoHizo = tarjeta.quienLoHizo,
                destinoFinal = tarjeta.destinoFinal,
                fechaFinal = tarjeta.fechaFinal,
                priority = tarjeta.priority.name.lowercase(),
                assignedToId = tarjeta.assignedTo?.id,
                imageUris = tarjeta.images.map { it.imageUrl }
            )
            
            val response = tarjetasApi.createTarjeta(createRequest)
            
            if (response.isSuccessful && response.body() != null) {
                val tarjetaDto = response.body()!!
                val nuevaTarjeta = tarjetaDto.toDomainModel()
                
                Log.d("TarjetasRepository", "Tarjeta creada exitosamente: ${nuevaTarjeta.numero}")
                emit(Resource.Success(nuevaTarjeta))
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Error al crear tarjeta"
                Log.e("TarjetasRepository", "Error API: $errorMessage")
                emit(Resource.Error(errorMessage))
            }
        } catch (e: Exception) {
            Log.e("TarjetasRepository", "Error de conexión: ${e.localizedMessage}", e)
            emit(Resource.Error(e.localizedMessage ?: "Error de conexión"))
        }
    }

    override suspend fun createTarjeta(request: CreateTarjetaRequest): Flow<Resource<TarjetaRoja>> = flow {
        emit(Resource.Loading())
        
        try {
            Log.d("TarjetasRepository", "Creando tarjeta con datos: $request")
            
            val createRequest = com.cocido.formokaizen.data.remote.dto.CreateTarjetaRequestDto(
                numero = request.numero,
                fecha = request.fecha,
                sector = request.sector,
                descripcion = request.descripcion,
                motivo = request.motivo,
                quienLoHizo = request.quienLoHizo,
                destinoFinal = request.destinoFinal,
                fechaFinal = request.fechaFinal,
                priority = request.priority.name.lowercase(),
                assignedToId = request.assignedToId,
                imageUris = request.imageUris
            )
            
            val response = tarjetasApi.createTarjeta(createRequest)
            
            if (response.isSuccessful && response.body() != null) {
                val tarjetaDto = response.body()!!
                val tarjeta = tarjetaDto.toDomainModel()
                
                Log.d("TarjetasRepository", "Tarjeta creada exitosamente: ${tarjeta.numero}")
                emit(Resource.Success(tarjeta))
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Error al crear tarjeta"
                Log.e("TarjetasRepository", "Error API: $errorMessage")
                emit(Resource.Error(errorMessage))
            }
        } catch (e: Exception) {
            Log.e("TarjetasRepository", "Error de conexión: ${e.localizedMessage}", e)
            emit(Resource.Error(e.localizedMessage ?: "Error de conexión"))
        }
    }

    override suspend fun updateTarjeta(tarjeta: TarjetaRoja): Flow<Resource<TarjetaRoja>> {
        return flowOf(Resource.Success(tarjeta))
    }

    override suspend fun deleteTarjeta(id: Int): Flow<Resource<Unit>> {
        return flowOf(Resource.Success(Unit))
    }

    override suspend fun approveTarjeta(id: Int, action: String, comment: String?): Flow<Resource<TarjetaRoja>> {
        return flowOf(Resource.Error("No implementado"))
    }

    override suspend fun rejectTarjeta(id: Int, comment: String?): Flow<Resource<TarjetaRoja>> {
        return flowOf(Resource.Error("No implementado"))
    }

    override suspend fun markAsInProgress(id: Int): Flow<Resource<TarjetaRoja>> {
        return flowOf(Resource.Error("No implementado"))
    }

    override suspend fun markAsResolved(id: Int, resolution: String): Flow<Resource<TarjetaRoja>> {
        return flowOf(Resource.Error("No implementado"))
    }

    override suspend fun addComment(tarjetaId: Int, comment: String, isInternal: Boolean): Flow<Resource<Unit>> {
        return flowOf(Resource.Success(Unit))
    }

    override suspend fun uploadImage(tarjetaId: Int, imagePath: String, description: String): Flow<Resource<Unit>> {
        return flowOf(Resource.Success(Unit))
    }

    override suspend fun getDashboardStats(): Flow<Resource<Map<String, Any>>> {
        return flowOf(Resource.Success(emptyMap()))
    }

    override fun getTarjetasOffline(): Flow<List<TarjetaRoja>> {
        return flowOf(emptyList())
    }

    override suspend fun syncTarjetas(): Flow<Resource<Unit>> {
        return flowOf(Resource.Success(Unit))
    }

    override suspend fun validateNumero(numero: String, excludeId: Int?): Flow<Resource<Boolean>> {
        // Mock validation - numero is valid if it doesn't start with "0000"
        val isValid = !numero.startsWith("0000")
        return flowOf(Resource.Success(isValid))
    }
}