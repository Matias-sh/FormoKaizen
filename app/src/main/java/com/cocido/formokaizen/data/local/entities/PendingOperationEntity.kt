package com.cocido.formokaizen.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "pending_operations")
data class PendingOperationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val operationType: String, // CREATE, UPDATE, DELETE
    val entityType: String, // TARJETA, COMMENT, USER
    val entityId: String?, // ID del entity afectado (null para CREATE)
    val operationData: String, // JSON con los datos de la operación
    val createdAt: LocalDateTime,
    val retryCount: Int = 0,
    val lastRetryAt: LocalDateTime?,
    val errorMessage: String?
)