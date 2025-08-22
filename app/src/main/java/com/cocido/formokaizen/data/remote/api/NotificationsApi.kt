package com.cocido.formokaizen.data.remote.api

import com.cocido.formokaizen.data.remote.dto.NotificationDto
import retrofit2.Response
import retrofit2.http.*

interface NotificationsApi {
    
    @GET("notifications/")
    suspend fun getNotifications(
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 50,
        @Query("unread_only") unreadOnly: Boolean = false,
        @Query("type") type: String? = null,
        @Query("priority") priority: String? = null
    ): Response<com.cocido.formokaizen.data.remote.api.PaginatedResponse<NotificationDto>>
    
    @POST("notifications/{id}/mark-read/")
    suspend fun markAsRead(@Path("id") id: Int): Response<Unit>
    
    @POST("notifications/mark-all-read/")
    suspend fun markAllAsRead(): Response<Unit>
    
    @DELETE("notifications/{id}/")
    suspend fun deleteNotification(@Path("id") id: Int): Response<Unit>
    
    @DELETE("notifications/clear-all/")
    suspend fun clearAll(): Response<Unit>
    
    @GET("notifications/unread-count/")
    suspend fun getUnreadCount(): Response<Map<String, Int>>
}