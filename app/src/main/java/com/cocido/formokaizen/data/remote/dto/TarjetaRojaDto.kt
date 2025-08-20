package com.cocido.formokaizen.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.cocido.formokaizen.domain.entities.*

data class TarjetaRojaDto(
    val id: Int,
    val numero: String,
    val fecha: String,
    val sector: String,
    val descripcion: String,
    val motivo: String,
    @SerializedName("quien_lo_hizo")
    val quienLoHizo: String,
    @SerializedName("destino_final")
    val destinoFinal: String,
    @SerializedName("fecha_final")
    val fechaFinal: String?,
    @SerializedName("created_by")
    val createdBy: UserDto,
    @SerializedName("assigned_to")
    val assignedTo: UserDto?,
    @SerializedName("approved_by")
    val approvedBy: UserDto?,
    val status: String,
    val priority: String,
    val images: List<TarjetaImageDto>?,
    val comments: List<TarjetaCommentDto>?,
    val history: List<TarjetaHistoryDto>?,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("approved_at")
    val approvedAt: String?,
    @SerializedName("closed_at")
    val closedAt: String?,
    @SerializedName("is_overdue")
    val isOverdue: Boolean,
    @SerializedName("days_open")
    val daysOpen: Int
)

data class TarjetaImageDto(
    val id: Int,
    val image: String,
    val description: String,
    @SerializedName("uploaded_by")
    val uploadedBy: UserDto,
    @SerializedName("uploaded_at")
    val uploadedAt: String
)

data class TarjetaCommentDto(
    val id: Int,
    val comment: String,
    @SerializedName("is_internal")
    val isInternal: Boolean,
    val user: UserDto,
    @SerializedName("created_at")
    val createdAt: String
)

data class TarjetaHistoryDto(
    val id: Int,
    @SerializedName("tarjeta_id")
    val tarjetaId: Int,
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("user_name")
    val userName: String,
    val action: String,
    val field: String?,
    @SerializedName("old_value")
    val oldValue: String?,
    @SerializedName("new_value")
    val newValue: String?,
    val timestamp: String,
    val description: String
)

data class CreateTarjetaRequestDto(
    val numero: String,
    val fecha: String,
    val sector: String,
    val descripcion: String,
    val motivo: String,
    @SerializedName("quien_lo_hizo")
    val quienLoHizo: String,
    @SerializedName("destino_final")
    val destinoFinal: String,
    @SerializedName("fecha_final")
    val fechaFinal: String?,
    val priority: String,
    @SerializedName("assigned_to_id")
    val assignedToId: Int?,
    @SerializedName("image_uris")
    val imageUris: List<String> = emptyList()
)

data class ApproveTarjetaRequest(
    val action: String,
    val comment: String?
)

fun TarjetaRojaDto.toDomainModel(): TarjetaRoja {
    return TarjetaRoja(
        id = id,
        numero = numero,
        fecha = fecha,
        sector = sector,
        descripcion = descripcion,
        motivo = motivo,
        quienLoHizo = quienLoHizo,
        destinoFinal = destinoFinal,
        fechaFinal = fechaFinal,
        createdBy = createdBy.toDomainModel(),
        assignedTo = assignedTo?.toDomainModel(),
        approvedBy = approvedBy?.toDomainModel(),
        status = when (status.lowercase()) {
            "open" -> TarjetaStatus.OPEN
            "pending_approval" -> TarjetaStatus.PENDING_APPROVAL
            "approved" -> TarjetaStatus.APPROVED
            "in_progress" -> TarjetaStatus.IN_PROGRESS
            "resolved" -> TarjetaStatus.RESOLVED
            "closed" -> TarjetaStatus.CLOSED
            "rejected" -> TarjetaStatus.REJECTED
            else -> TarjetaStatus.OPEN
        },
        priority = when (priority.lowercase()) {
            "low" -> TarjetaPriority.LOW
            "medium" -> TarjetaPriority.MEDIUM
            "high" -> TarjetaPriority.HIGH
            "critical" -> TarjetaPriority.CRITICAL
            else -> TarjetaPriority.MEDIUM
        },
        images = images?.map { it.toDomainModel() } ?: emptyList(),
        comments = comments?.map { it.toDomainModel() } ?: emptyList(),
        history = history?.map { it.toDomainModel() } ?: emptyList(),
        createdAt = createdAt,
        updatedAt = updatedAt,
        approvedAt = approvedAt,
        closedAt = closedAt,
        isOverdue = isOverdue,
        daysOpen = daysOpen
    )
}

fun TarjetaImageDto.toDomainModel(): TarjetaImage {
    return TarjetaImage(
        id = id,
        imageUrl = image,
        description = description,
        uploadedBy = uploadedBy.toDomainModel(),
        uploadedAt = uploadedAt
    )
}

fun TarjetaCommentDto.toDomainModel(): TarjetaComment {
    return TarjetaComment(
        id = id,
        comment = comment,
        isInternal = isInternal,
        user = user.toDomainModel(),
        createdAt = createdAt
    )
}

fun TarjetaHistoryDto.toDomainModel(): TarjetaHistory {
    return TarjetaHistory(
        id = id,
        tarjetaId = tarjetaId,
        userId = userId,
        userName = userName,
        action = TarjetaAction.valueOf(action),
        field = field,
        oldValue = oldValue,
        newValue = newValue,
        timestamp = java.time.LocalDateTime.parse(timestamp),
        description = description
    )
}