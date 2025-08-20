package com.cocido.formokaizen.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cocido.formokaizen.domain.entities.User
import com.cocido.formokaizen.domain.entities.UserRole

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val id: Int,
    val username: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val role: String,
    val employeeId: String?,
    val phone: String?,
    val department: String?,
    val position: String?,
    val avatar: String?,
    val isActive: Boolean,
    val createdAt: String,
    val syncStatus: SyncStatus = SyncStatus.SYNCED,
    val lastModified: Long = System.currentTimeMillis()
)

fun UserEntity.toDomainModel(): User {
    return User(
        id = id,
        username = username,
        email = email,
        firstName = firstName,
        lastName = lastName,
        role = when (role.uppercase()) {
            "ADMIN" -> UserRole.ADMIN
            "SUPERVISOR" -> UserRole.SUPERVISOR
            else -> UserRole.USER
        },
        employeeId = employeeId,
        phone = phone,
        department = department,
        position = position,
        avatar = avatar,
        isActive = isActive,
        createdAt = createdAt
    )
}

fun User.toEntity(): UserEntity {
    return UserEntity(
        id = id,
        username = username,
        email = email,
        firstName = firstName,
        lastName = lastName,
        role = role.name,
        employeeId = employeeId,
        phone = phone,
        department = department,
        position = position,
        avatar = avatar,
        isActive = isActive,
        createdAt = createdAt
    )
}

enum class SyncStatus {
    SYNCED,
    PENDING_SYNC,
    PENDING_CREATION,
    PENDING_UPDATE,
    PENDING_DELETION,
    SYNC_ERROR
}