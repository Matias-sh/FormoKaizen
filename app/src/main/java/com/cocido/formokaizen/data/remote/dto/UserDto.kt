package com.cocido.formokaizen.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.cocido.formokaizen.domain.entities.User
import com.cocido.formokaizen.domain.entities.UserRole

data class UserDto(
    val id: Int,
    val username: String,
    val email: String,
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("last_name")
    val lastName: String,
    val role: String,
    @SerializedName("employee_id")
    val employeeId: String?,
    val phone: String?,
    val department: String?,
    val position: String?,
    val avatar: String?,
    @SerializedName("is_active")
    val isActive: Boolean,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("full_name")
    val fullName: String?
)

fun UserDto.toDomainModel(): User {
    return User(
        id = id,
        username = username,
        email = email,
        firstName = firstName,
        lastName = lastName,
        role = when (role.lowercase()) {
            "admin" -> UserRole.ADMIN
            "supervisor" -> UserRole.SUPERVISOR
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

fun User.toDto(): UserDto {
    return UserDto(
        id = id,
        username = username,
        email = email,
        firstName = firstName,
        lastName = lastName,
        role = role.name.lowercase(),
        employeeId = employeeId,
        phone = phone,
        department = department,
        position = position,
        avatar = avatar,
        isActive = isActive,
        createdAt = createdAt,
        fullName = fullName
    )
}