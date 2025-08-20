package com.cocido.formokaizen.data.local.entities

import androidx.room.*
import com.cocido.formokaizen.domain.entities.Category
import com.cocido.formokaizen.domain.entities.WorkArea

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val description: String,
    val color: String,
    val icon: String,
    val isActive: Boolean,
    val createdById: Int?,
    val createdAt: String,
    val tarjetasCount: Int = 0,
    val openTarjetasCount: Int = 0,
    val syncStatus: SyncStatus = SyncStatus.SYNCED,
    val lastModified: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "work_areas",
    foreignKeys = [
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["categoryId"])]
)
data class WorkAreaEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val description: String,
    val categoryId: Int,
    val responsibleId: Int?,
    val isActive: Boolean,
    val createdAt: String,
    val syncStatus: SyncStatus = SyncStatus.SYNCED,
    val lastModified: Long = System.currentTimeMillis()
)

data class CategoryWithWorkAreas(
    @Embedded val category: CategoryEntity,
    
    @Relation(
        parentColumn = "id",
        entityColumn = "categoryId"
    )
    val workAreas: List<WorkAreaEntity>,
    
    @Relation(
        parentColumn = "createdById",
        entityColumn = "id"
    )
    val createdBy: UserEntity?
)

fun CategoryEntity.toDomainModel(workAreas: List<WorkArea>): Category {
    return Category(
        id = id,
        name = name,
        description = description,
        color = color,
        icon = icon,
        isActive = isActive,
        workAreas = workAreas,
        createdBy = null, // Will be populated by relations
        createdAt = createdAt,
        tarjetasCount = tarjetasCount,
        openTarjetasCount = openTarjetasCount
    )
}

fun CategoryWithWorkAreas.toDomainModel(): Category {
    return Category(
        id = category.id,
        name = category.name,
        description = category.description,
        color = category.color,
        icon = category.icon,
        isActive = category.isActive,
        workAreas = workAreas.map { it.toDomainModel() },
        createdBy = createdBy?.toDomainModel(),
        createdAt = category.createdAt,
        tarjetasCount = category.tarjetasCount,
        openTarjetasCount = category.openTarjetasCount
    )
}

fun WorkAreaEntity.toDomainModel(): WorkArea {
    return WorkArea(
        id = id,
        name = name,
        description = description,
        categoryId = categoryId,
        responsible = null, // Will be populated separately
        isActive = isActive,
        createdAt = createdAt
    )
}

fun Category.toEntity(): CategoryEntity {
    return CategoryEntity(
        id = id,
        name = name,
        description = description,
        color = color,
        icon = icon,
        isActive = isActive,
        createdById = createdBy?.id,
        createdAt = createdAt,
        tarjetasCount = tarjetasCount,
        openTarjetasCount = openTarjetasCount
    )
}

fun WorkArea.toEntity(): WorkAreaEntity {
    return WorkAreaEntity(
        id = id,
        name = name,
        description = description,
        categoryId = categoryId,
        responsibleId = responsible?.id,
        isActive = isActive,
        createdAt = createdAt
    )
}