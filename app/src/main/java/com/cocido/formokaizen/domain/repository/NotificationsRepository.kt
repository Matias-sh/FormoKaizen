package com.cocido.formokaizen.domain.repository

import com.cocido.formokaizen.domain.entities.Notification
import com.cocido.formokaizen.domain.entities.NotificationPreference
import com.cocido.formokaizen.utils.Resource
import kotlinx.coroutines.flow.Flow

interface NotificationsRepository {
    suspend fun getNotifications(
        page: Int = 1,
        perPage: Int = 50,
        unreadOnly: Boolean = false,
        type: String? = null,
        priority: String? = null
    ): Flow<Resource<List<Notification>>>
    
    suspend fun markNotificationAsRead(id: Int): Flow<Resource<Unit>>
    suspend fun markAllNotificationsAsRead(): Flow<Resource<Unit>>
    suspend fun deleteNotification(id: Int): Flow<Resource<Unit>>
    suspend fun clearAllNotifications(): Flow<Resource<Unit>>
    
    suspend fun getNotificationPreferences(): Flow<Resource<NotificationPreference>>
    suspend fun updateNotificationPreferences(preferences: NotificationPreference): Flow<Resource<NotificationPreference>>
    
    suspend fun registerDeviceToken(token: String, platform: String, deviceName: String): Flow<Resource<Unit>>
    
    suspend fun getNotificationStats(): Flow<Resource<Map<String, Any>>>
    
    // Offline support
    fun getNotificationsOffline(): Flow<List<Notification>>
    fun getUnreadCount(): Flow<Int>
    suspend fun syncNotifications(): Flow<Resource<Unit>>
}