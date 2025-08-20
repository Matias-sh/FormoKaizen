package com.cocido.formokaizen.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.cocido.formokaizen.domain.entities.Category
import com.cocido.formokaizen.domain.entities.WorkArea

data class CategoryDto(
    val id: Int,
    val name: String,
    val description: String,
    val color: String,
    val icon: String,
    @SerializedName("is_active")
    val isActive: Boolean,
    @SerializedName("work_areas")
    val workAreas: List<WorkAreaDto>?,
    @SerializedName("created_by")
    val createdBy: UserDto?,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("tarjetas_count")
    val tarjetasCount: Int?,
    @SerializedName("open_tarjetas_count")
    val openTarjetasCount: Int?
)

data class WorkAreaDto(
    val id: Int,
    val name: String,
    val description: String,
    val responsible: UserDto?,
    @SerializedName("is_active")
    val isActive: Boolean,
    @SerializedName("created_at")
    val createdAt: String
)

fun CategoryDto.toDomainModel(): Category {
    return Category(
        id = id,
        name = name,
        description = description,
        color = color,
        icon = icon,
        isActive = isActive,
        workAreas = workAreas?.map { it.toDomainModel() } ?: emptyList(),
        createdBy = createdBy?.toDomainModel(),
        createdAt = createdAt,
        tarjetasCount = tarjetasCount ?: 0,
        openTarjetasCount = openTarjetasCount ?: 0
    )
}

fun WorkAreaDto.toDomainModel(): WorkArea {
    return WorkArea(
        id = id,
        name = name,
        description = description,
        categoryId = 0, // This will be set by the parent category
        responsible = responsible?.toDomainModel(),
        isActive = isActive,
        createdAt = createdAt
    )
}