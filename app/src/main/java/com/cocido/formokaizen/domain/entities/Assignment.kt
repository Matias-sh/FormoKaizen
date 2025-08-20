package com.cocido.formokaizen.domain.entities

import java.time.LocalDateTime

data class Assignment(
    val id: Int,
    val tarjetaId: Int,
    val assignedById: Int,
    val assignedByName: String,
    val assignedToId: Int,
    val assignedToName: String,
    val assignedToEmail: String,
    val assignedAt: LocalDateTime,
    val dueDate: LocalDateTime?,
    val notes: String?,
    val status: AssignmentStatus,
    val acceptedAt: LocalDateTime?,
    val completedAt: LocalDateTime?
)

enum class AssignmentStatus {
    PENDING,
    ACCEPTED,
    IN_PROGRESS,
    COMPLETED,
    REJECTED,
    REASSIGNED
}

data class AssignTarjetaRequest(
    val tarjetaId: Int,
    val assignedToId: Int,
    val dueDate: LocalDateTime?,
    val notes: String?
)

data class AssignmentResponse(
    val assignmentId: Int,
    val action: AssignmentAction,
    val notes: String?
)

enum class AssignmentAction {
    ACCEPT,
    REJECT,
    COMPLETE,
    REQUEST_REASSIGNMENT
}

data class TeamMember(
    val id: Int,
    val username: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val department: String?,
    val position: String?,
    val avatar: String?,
    val isActive: Boolean,
    val workload: Int, // Número de tarjetas asignadas actualmente
    val availability: UserAvailability
)

enum class UserAvailability {
    AVAILABLE,
    BUSY,
    OVERLOADED,
    ON_LEAVE,
    OFFLINE
}