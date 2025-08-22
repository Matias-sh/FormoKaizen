package com.cocido.formokaizen.data.repository

import android.util.Log
import com.cocido.formokaizen.data.remote.api.NotificationsApi
import com.cocido.formokaizen.data.remote.dto.toDomainModel
import com.cocido.formokaizen.domain.entities.Notification
import com.cocido.formokaizen.domain.entities.NotificationPreference
import com.cocido.formokaizen.domain.repository.NotificationsRepository
import com.cocido.formokaizen.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class NotificationsRepositoryImpl @Inject constructor(
    private val notificationsApi: NotificationsApi
) : NotificationsRepository {
    
    override suspend fun getNotifications(
        page: Int,
        perPage: Int,
        unreadOnly: Boolean,
        type: String?,
        priority: String?
    ): Flow<Resource<List<Notification>>> = flow {
        emit(Resource.Loading())
        
        try {
            Log.d("NotificationsRepository", "Obteniendo notificaciones")
            
            val response = notificationsApi.getNotifications(
                page = page,
                perPage = perPage,
                unreadOnly = unreadOnly,
                type = type,
                priority = priority
            )
            
            if (response.isSuccessful && response.body() != null) {
                val paginatedResponse = response.body()!!
                val notifications = paginatedResponse.results.map { it.toDomainModel() }
                
                Log.d("NotificationsRepository", "Notificaciones obtenidas: ${notifications.size}")
                emit(Resource.Success(notifications))
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Error al obtener notificaciones"
                Log.e("NotificationsRepository", "Error API: $errorMessage")
                emit(Resource.Error(errorMessage))
            }
        } catch (e: Exception) {
            Log.e("NotificationsRepository", "Error de conexión: ${e.localizedMessage}", e)
            emit(Resource.Error(e.localizedMessage ?: "Error de conexión"))
        }
    }
    
    override suspend fun markNotificationAsRead(id: Int): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        
        try {
            val response = notificationsApi.markAsRead(id)
            
            if (response.isSuccessful) {
                Log.d("NotificationsRepository", "Notificación marcada como leída: $id")
                emit(Resource.Success(Unit))
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Error al marcar notificación"
                Log.e("NotificationsRepository", "Error API: $errorMessage")
                emit(Resource.Error(errorMessage))
            }
        } catch (e: Exception) {
            Log.e("NotificationsRepository", "Error de conexión: ${e.localizedMessage}", e)
            emit(Resource.Error(e.localizedMessage ?: "Error de conexión"))
        }
    }
    
    override suspend fun markAllNotificationsAsRead(): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        
        try {
            val response = notificationsApi.markAllAsRead()
            
            if (response.isSuccessful) {
                Log.d("NotificationsRepository", "Todas las notificaciones marcadas como leídas")
                emit(Resource.Success(Unit))
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Error al marcar todas las notificaciones"
                Log.e("NotificationsRepository", "Error API: $errorMessage")
                emit(Resource.Error(errorMessage))
            }
        } catch (e: Exception) {
            Log.e("NotificationsRepository", "Error de conexión: ${e.localizedMessage}", e)
            emit(Resource.Error(e.localizedMessage ?: "Error de conexión"))
        }
    }
    
    override suspend fun deleteNotification(id: Int): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        
        try {
            val response = notificationsApi.deleteNotification(id)
            
            if (response.isSuccessful) {
                Log.d("NotificationsRepository", "Notificación eliminada: $id")
                emit(Resource.Success(Unit))
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Error al eliminar notificación"
                Log.e("NotificationsRepository", "Error API: $errorMessage")
                emit(Resource.Error(errorMessage))
            }
        } catch (e: Exception) {
            Log.e("NotificationsRepository", "Error de conexión: ${e.localizedMessage}", e)
            emit(Resource.Error(e.localizedMessage ?: "Error de conexión"))
        }
    }
    
    override suspend fun clearAllNotifications(): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        
        try {
            val response = notificationsApi.clearAll()
            
            if (response.isSuccessful) {
                Log.d("NotificationsRepository", "Todas las notificaciones eliminadas")
                emit(Resource.Success(Unit))
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Error al limpiar notificaciones"
                Log.e("NotificationsRepository", "Error API: $errorMessage")
                emit(Resource.Error(errorMessage))
            }
        } catch (e: Exception) {
            Log.e("NotificationsRepository", "Error de conexión: ${e.localizedMessage}", e)
            emit(Resource.Error(e.localizedMessage ?: "Error de conexión"))
        }
    }
    
    override suspend fun getNotificationPreferences(): Flow<Resource<NotificationPreference>> {
        return flowOf(Resource.Error("No implementado"))
    }
    
    override suspend fun updateNotificationPreferences(preferences: NotificationPreference): Flow<Resource<NotificationPreference>> {
        return flowOf(Resource.Error("No implementado"))
    }
    
    override suspend fun registerDeviceToken(token: String, platform: String, deviceName: String): Flow<Resource<Unit>> {
        return flowOf(Resource.Success(Unit))
    }
    
    override suspend fun getNotificationStats(): Flow<Resource<Map<String, Any>>> {
        return flowOf(Resource.Success(emptyMap()))
    }
    
    override fun getNotificationsOffline(): Flow<List<Notification>> {
        return flowOf(emptyList())
    }
    
    override fun getUnreadCount(): Flow<Int> {
        return flowOf(0)
    }
    
    override suspend fun syncNotifications(): Flow<Resource<Unit>> {
        return flowOf(Resource.Success(Unit))
    }
}