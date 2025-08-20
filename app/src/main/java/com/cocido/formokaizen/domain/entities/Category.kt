package com.cocido.formokaizen.domain.entities

data class Category(
    val id: Int,
    val name: String,
    val description: String,
    val color: String,
    val icon: String,
    val isActive: Boolean,
    val workAreas: List<WorkArea>,
    val createdBy: User?,
    val createdAt: String,
    val tarjetasCount: Int,
    val openTarjetasCount: Int
)

data class WorkArea(
    val id: Int,
    val name: String,
    val description: String,
    val categoryId: Int,
    val responsible: User?,
    val isActive: Boolean,
    val createdAt: String
)