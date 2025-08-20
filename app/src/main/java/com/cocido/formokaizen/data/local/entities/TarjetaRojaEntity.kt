package com.cocido.formokaizen.data.local.entities

import androidx.room.*
import com.cocido.formokaizen.domain.entities.*

@Entity(
    tableName = "tarjetas_rojas",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["createdById"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["assignedToId"],
            onDelete = ForeignKey.SET_NULL
        ),
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["createdById"]),
        Index(value = ["assignedToId"]),
        Index(value = ["categoryId"]),
        Index(value = ["status"]),
        Index(value = ["priority"])
    ]
)
data class TarjetaRojaEntity(
    @PrimaryKey
    val id: Int,
    val code: String,
    val title: String,
    val description: String,
    val categoryId: Int,
    val workAreaId: Int?,
    val status: String,
    val priority: String,
    val sector: String,
    val motivo: String,
    val destinoFinal: String,
    val createdById: Int,
    val assignedToId: Int?,
    val approvedById: Int?,
    val estimatedResolutionDate: String?,
    val actualResolutionDate: String?,
    val resolutionNotes: String?,
    val createdAt: String,
    val updatedAt: String,
    val approvedAt: String?,
    val closedAt: String?,
    val isOverdue: Boolean,
    val daysOpen: Int,
    val syncStatus: SyncStatus = SyncStatus.SYNCED,
    val lastModified: Long = System.currentTimeMillis()
)

data class TarjetaRojaWithRelations(
    @Embedded val tarjeta: TarjetaRojaEntity,
    
    @Relation(
        parentColumn = "createdById",
        entityColumn = "id"
    )
    val createdBy: UserEntity,
    
    @Relation(
        parentColumn = "assignedToId",
        entityColumn = "id"
    )
    val assignedTo: UserEntity?,
    
    @Relation(
        parentColumn = "approvedById",
        entityColumn = "id"
    )
    val approvedBy: UserEntity?,
    
    @Relation(
        parentColumn = "categoryId",
        entityColumn = "id"
    )
    val category: CategoryEntity,
    
    @Relation(
        parentColumn = "workAreaId",
        entityColumn = "id"
    )
    val workArea: WorkAreaEntity?,
    
    @Relation(
        parentColumn = "id",
        entityColumn = "tarjetaId"
    )
    val images: List<TarjetaImageEntity>,
    
    @Relation(
        parentColumn = "id",
        entityColumn = "tarjetaId"
    )
    val comments: List<TarjetaCommentEntity>,
    
    @Relation(
        parentColumn = "id",
        entityColumn = "tarjetaId"
    )
    val history: List<TarjetaHistoryEntity>
)

@Entity(
    tableName = "tarjeta_images",
    foreignKeys = [
        ForeignKey(
            entity = TarjetaRojaEntity::class,
            parentColumns = ["id"],
            childColumns = ["tarjetaId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["tarjetaId"])]
)
data class TarjetaImageEntity(
    @PrimaryKey
    val id: Int,
    val tarjetaId: Int,
    val imageUrl: String,
    val localImagePath: String?, // For offline images
    val description: String,
    val uploadedById: Int,
    val uploadedAt: String,
    val syncStatus: SyncStatus = SyncStatus.SYNCED
)

@Entity(
    tableName = "tarjeta_comments",
    foreignKeys = [
        ForeignKey(
            entity = TarjetaRojaEntity::class,
            parentColumns = ["id"],
            childColumns = ["tarjetaId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["tarjetaId"])]
)
data class TarjetaCommentEntity(
    @PrimaryKey
    val id: Int,
    val tarjetaId: Int,
    val comment: String,
    val isInternal: Boolean,
    val userId: Int,
    val createdAt: String,
    val syncStatus: SyncStatus = SyncStatus.SYNCED
)

@Entity(
    tableName = "tarjeta_history",
    foreignKeys = [
        ForeignKey(
            entity = TarjetaRojaEntity::class,
            parentColumns = ["id"],
            childColumns = ["tarjetaId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["tarjetaId"])]
)
data class TarjetaHistoryEntity(
    @PrimaryKey
    val id: Int,
    val tarjetaId: Int,
    val userId: Int,
    val userName: String,
    val action: String,
    val field: String?,
    val oldValue: String?,
    val newValue: String?,
    val timestamp: String,
    val description: String,
    val syncStatus: SyncStatus = SyncStatus.SYNCED
)

fun TarjetaRojaWithRelations.toDomainModel(): TarjetaRoja {
    return TarjetaRoja(
        id = tarjeta.id,
        code = tarjeta.code,
        title = tarjeta.title,
        description = tarjeta.description,
        category = category.toDomainModel(emptyList()), // Work areas loaded separately
        workArea = workArea?.toDomainModel(),
        status = when (tarjeta.status.uppercase()) {
            "OPEN" -> TarjetaStatus.OPEN
            "PENDING_APPROVAL" -> TarjetaStatus.PENDING_APPROVAL
            "APPROVED" -> TarjetaStatus.APPROVED
            "IN_PROGRESS" -> TarjetaStatus.IN_PROGRESS
            "RESOLVED" -> TarjetaStatus.RESOLVED
            "CLOSED" -> TarjetaStatus.CLOSED
            "REJECTED" -> TarjetaStatus.REJECTED
            else -> TarjetaStatus.OPEN
        },
        priority = when (tarjeta.priority.uppercase()) {
            "LOW" -> TarjetaPriority.LOW
            "MEDIUM" -> TarjetaPriority.MEDIUM
            "HIGH" -> TarjetaPriority.HIGH
            "CRITICAL" -> TarjetaPriority.CRITICAL
            else -> TarjetaPriority.MEDIUM
        },
        sector = tarjeta.sector,
        motivo = tarjeta.motivo,
        destinoFinal = tarjeta.destinoFinal,
        createdBy = createdBy.toDomainModel(),
        assignedTo = assignedTo?.toDomainModel(),
        approvedBy = approvedBy?.toDomainModel(),
        estimatedResolutionDate = tarjeta.estimatedResolutionDate,
        actualResolutionDate = tarjeta.actualResolutionDate,
        resolutionNotes = tarjeta.resolutionNotes,
        images = images.map { it.toDomainModel() },
        comments = comments.map { it.toDomainModel() },
        history = history.map { it.toDomainModel() },
        createdAt = tarjeta.createdAt,
        updatedAt = tarjeta.updatedAt,
        approvedAt = tarjeta.approvedAt,
        closedAt = tarjeta.closedAt,
        isOverdue = tarjeta.isOverdue,
        daysOpen = tarjeta.daysOpen
    )
}

fun TarjetaImageEntity.toDomainModel(): TarjetaImage {
    return TarjetaImage(
        id = id,
        imageUrl = imageUrl,
        description = description,
        uploadedBy = User(id = uploadedById, username = "", email = "", firstName = "", lastName = "", role = UserRole.USER, employeeId = null, phone = null, department = null, position = null, avatar = null, isActive = true, createdAt = ""),
        uploadedAt = uploadedAt
    )
}

fun TarjetaCommentEntity.toDomainModel(): TarjetaComment {
    return TarjetaComment(
        id = id,
        comment = comment,
        isInternal = isInternal,
        user = User(id = userId, username = "", email = "", firstName = "", lastName = "", role = UserRole.USER, employeeId = null, phone = null, department = null, position = null, avatar = null, isActive = true, createdAt = ""),
        createdAt = createdAt
    )
}

fun TarjetaHistoryEntity.toDomainModel(): TarjetaHistory {
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