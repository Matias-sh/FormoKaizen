package com.cocido.formokaizen.domain.entities

data class Notification(
    val id: Int,
    val notificationType: NotificationType,
    val title: String,
    val message: String,
    val priority: NotificationPriority,
    val sender: User?,
    val isRead: Boolean,
    val readAt: String?,
    val createdAt: String,
    val contentObjectData: ContentObjectData?
)

enum class NotificationType {
    TARJETA_CREATED,
    TARJETA_ASSIGNED,
    TARJETA_APPROVED,
    TARJETA_REJECTED,
    TARJETA_COMPLETED,
    TARJETA_OVERDUE,
    COMMENT_ADDED,
    TEAM_ADDED,
    TEAM_REMOVED,
    PROJECT_ASSIGNED,
    SYSTEM_UPDATE
}

enum class NotificationPriority {
    LOW, NORMAL, HIGH, URGENT
}

data class ContentObjectData(
    val id: Int,
    val type: String,
    val displayName: String
)

data class NotificationPreference(
    val emailTarjetaCreated: Boolean,
    val emailTarjetaAssigned: Boolean,
    val emailTarjetaApproved: Boolean,
    val emailTarjetaOverdue: Boolean,
    val emailCommentAdded: Boolean,
    val emailTeamChanges: Boolean,
    val pushTarjetaCreated: Boolean,
    val pushTarjetaAssigned: Boolean,
    val pushTarjetaApproved: Boolean,
    val pushTarjetaOverdue: Boolean,
    val pushCommentAdded: Boolean,
    val pushTeamChanges: Boolean,
    val quietHoursStart: String,
    val quietHoursEnd: String,
    val weekendNotifications: Boolean
)