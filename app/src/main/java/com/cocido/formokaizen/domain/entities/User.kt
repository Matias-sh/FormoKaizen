package com.cocido.formokaizen.domain.entities

data class User(
    val id: Int,
    val username: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val role: UserRole,
    val employeeId: String?,
    val phone: String?,
    val department: String?,
    val position: String?,
    val avatar: String?,
    val isActive: Boolean,
    val createdAt: String
) {
    val fullName: String
        get() = "$firstName $lastName".trim()
    
    fun isAdmin(): Boolean = role == UserRole.ADMIN
    fun isSupervisor(): Boolean = role in listOf(UserRole.ADMIN, UserRole.SUPERVISOR)
    fun canApprove(): Boolean = role in listOf(UserRole.ADMIN, UserRole.SUPERVISOR)
}

enum class UserRole {
    ADMIN, SUPERVISOR, USER
}