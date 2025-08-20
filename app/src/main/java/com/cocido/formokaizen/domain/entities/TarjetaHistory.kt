package com.cocido.formokaizen.domain.entities

import java.time.LocalDateTime

data class TarjetaHistory(
    val id: Int,
    val tarjetaId: Int,
    val userId: Int,
    val userName: String,
    val action: TarjetaAction,
    val field: String?,
    val oldValue: String?,
    val newValue: String?,
    val timestamp: LocalDateTime,
    val description: String
)

enum class TarjetaAction {
    CREATED,
    UPDATED,
    STATUS_CHANGED,
    ASSIGNED,
    UNASSIGNED,
    PRIORITY_CHANGED,
    CATEGORY_CHANGED,
    COMMENTED,
    ATTACHMENT_ADDED,
    ATTACHMENT_REMOVED,
    DUE_DATE_CHANGED,
    RESOLVED,
    REOPENED,
    ARCHIVED
}

data class TarjetaActivityFeed(
    val tarjetaId: Int,
    val activities: List<TarjetaHistory>,
    val comments: List<TarjetaComment>,
    val totalItems: Int,
    val hasMore: Boolean
)