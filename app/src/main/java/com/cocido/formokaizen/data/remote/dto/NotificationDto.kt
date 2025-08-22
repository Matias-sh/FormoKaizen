package com.cocido.formokaizen.data.remote.dto

import com.cocido.formokaizen.domain.entities.Notification
import com.cocido.formokaizen.domain.entities.NotificationPriority
import com.cocido.formokaizen.domain.entities.NotificationType
import com.cocido.formokaizen.domain.entities.ContentObjectData
import com.google.gson.annotations.SerializedName

data class NotificationDto(
    val id: Int,
    @SerializedName("notification_type")
    val notificationType: String,
    val title: String,
    val message: String,
    val priority: String,
    val sender: UserDto?,
    @SerializedName("is_read")
    val isRead: Boolean,
    @SerializedName("read_at")
    val readAt: String?,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("content_object_data")
    val contentObjectData: ContentObjectDataDto?
)

data class ContentObjectDataDto(
    val id: Int,
    val type: String,
    @SerializedName("display_name")
    val displayName: String?,
    val str: String?
)

fun NotificationDto.toDomainModel(): Notification {
    return Notification(
        id = id,
        notificationType = when (notificationType.uppercase()) {
            "TARJETA_CREATED" -> NotificationType.TARJETA_CREATED
            "TARJETA_ASSIGNED" -> NotificationType.TARJETA_ASSIGNED
            "TARJETA_APPROVED" -> NotificationType.TARJETA_APPROVED
            "TARJETA_REJECTED" -> NotificationType.TARJETA_REJECTED
            "TARJETA_COMPLETED" -> NotificationType.TARJETA_COMPLETED
            "TARJETA_OVERDUE" -> NotificationType.TARJETA_OVERDUE
            "COMMENT_ADDED" -> NotificationType.COMMENT_ADDED
            "TEAM_ADDED" -> NotificationType.TEAM_ADDED
            "TEAM_REMOVED" -> NotificationType.TEAM_REMOVED
            "PROJECT_ASSIGNED" -> NotificationType.PROJECT_ASSIGNED
            else -> NotificationType.SYSTEM_UPDATE
        },
        title = title,
        message = message,
        priority = when (priority.uppercase()) {
            "LOW" -> NotificationPriority.LOW
            "NORMAL" -> NotificationPriority.NORMAL
            "HIGH" -> NotificationPriority.HIGH
            "URGENT" -> NotificationPriority.URGENT
            else -> NotificationPriority.NORMAL
        },
        sender = sender?.toDomainModel(),
        isRead = isRead,
        readAt = readAt,
        createdAt = createdAt,
        contentObjectData = contentObjectData?.let {
            ContentObjectData(
                id = it.id,
                type = it.type,
                displayName = it.displayName ?: it.str ?: "Objeto sin nombre"
            )
        }
    )
}