package com.cocido.formokaizen.domain.entities

data class TarjetaRoja(
    val id: Int,
    val code: String,
    val title: String,
    val description: String,
    val category: Category,
    val workArea: WorkArea?,
    val status: TarjetaStatus,
    val priority: TarjetaPriority,
    val sector: String,
    val motivo: String,
    val destinoFinal: String,
    val createdBy: User,
    val assignedTo: User?,
    val approvedBy: User?,
    val estimatedResolutionDate: String?,
    val actualResolutionDate: String?,
    val resolutionNotes: String?,
    val images: List<TarjetaImage>,
    val comments: List<TarjetaComment>,
    val history: List<TarjetaHistory>,
    val createdAt: String,
    val updatedAt: String,
    val approvedAt: String?,
    val closedAt: String?,
    val isOverdue: Boolean,
    val daysOpen: Int
) {
    fun canBeEditedBy(user: User): Boolean {
        return (user.id == createdBy.id && status in listOf(TarjetaStatus.OPEN, TarjetaStatus.REJECTED)) ||
                user.isSupervisor()
    }
    
    fun canBeApprovedBy(user: User): Boolean {
        return user.canApprove() && status == TarjetaStatus.PENDING_APPROVAL
    }
}

enum class TarjetaStatus {
    OPEN,
    PENDING_APPROVAL,
    APPROVED,
    IN_PROGRESS,
    RESOLVED,
    CLOSED,
    REJECTED
}

enum class TarjetaPriority {
    LOW, MEDIUM, HIGH, CRITICAL
}

// Alias para compatibilidad
typealias Priority = TarjetaPriority

data class CreateTarjetaRequest(
    val title: String,
    val description: String,
    val categoryId: Int,
    val workAreaId: Int?,
    val priority: TarjetaPriority,
    val sector: String,
    val motivo: String,
    val destinoFinal: String,
    val estimatedResolutionDate: String?,
    val assignedToId: Int?,
    val imageUris: List<String> = emptyList()
)

data class TarjetaImage(
    val id: Int,
    val imageUrl: String,
    val description: String,
    val uploadedBy: User,
    val uploadedAt: String
)

data class TarjetaComment(
    val id: Int,
    val comment: String,
    val isInternal: Boolean,
    val user: User,
    val createdAt: String
)

