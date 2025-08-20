package com.cocido.formokaizen.domain.entities

import java.time.LocalDateTime

data class Team(
    val id: Int,
    val name: String,
    val description: String?,
    val departmentId: Int,
    val departmentName: String,
    val leaderId: Int,
    val leaderName: String,
    val members: List<TeamMember>,
    val isActive: Boolean,
    val createdAt: LocalDateTime,
    val settings: TeamSettings
)

data class TeamSettings(
    val allowSelfAssignment: Boolean = true,
    val requireApprovalForHighPriority: Boolean = true,
    val defaultAssignmentDuration: Int = 7, // días
    val enableTeamChat: Boolean = true,
    val enableFileSharing: Boolean = true,
    val maxConcurrentTasks: Int = 5
)

data class TeamInvitation(
    val id: Int,
    val teamId: Int,
    val teamName: String,
    val invitedUserId: Int,
    val invitedUserEmail: String,
    val invitedById: Int,
    val invitedByName: String,
    val role: TeamRole,
    val status: InvitationStatus,
    val message: String?,
    val createdAt: LocalDateTime,
    val expiresAt: LocalDateTime,
    val respondedAt: LocalDateTime?
)

enum class TeamRole {
    MEMBER,
    COORDINATOR,
    LEADER,
    ADMIN
}

enum class InvitationStatus {
    PENDING,
    ACCEPTED,
    DECLINED,
    EXPIRED,
    CANCELED
}

data class TeamActivity(
    val id: Int,
    val teamId: Int,
    val userId: Int,
    val userName: String,
    val activityType: TeamActivityType,
    val description: String,
    val metadata: Map<String, Any>?,
    val timestamp: LocalDateTime
)

enum class TeamActivityType {
    MEMBER_JOINED,
    MEMBER_LEFT,
    ROLE_CHANGED,
    TARJETA_ASSIGNED,
    TARJETA_COMPLETED,
    TEAM_CREATED,
    TEAM_UPDATED,
    MEETING_SCHEDULED,
    MILESTONE_REACHED
}

data class TeamStats(
    val teamId: Int,
    val totalMembers: Int,
    val activeTarjetas: Int,
    val completedTarjetas: Int,
    val averageResolutionTime: Double, // días
    val memberPerformance: List<MemberPerformance>,
    val categoryDistribution: Map<String, Int>,
    val priorityDistribution: Map<TarjetaPriority, Int>
)

data class MemberPerformance(
    val userId: Int,
    val userName: String,
    val assignedTasks: Int,
    val completedTasks: Int,
    val averageCompletionTime: Double,
    val qualityScore: Double, // 0.0 - 5.0
    val collaborationScore: Double // 0.0 - 5.0
)

data class TeamMeeting(
    val id: Int,
    val teamId: Int,
    val title: String,
    val description: String?,
    val scheduledDate: LocalDateTime,
    val duration: Int, // minutos
    val location: String?,
    val meetingLink: String?,
    val organizedById: Int,
    val organizerName: String,
    val attendees: List<MeetingAttendee>,
    val agenda: List<AgendaItem>,
    val status: MeetingStatus,
    val notes: String?
)

data class MeetingAttendee(
    val userId: Int,
    val userName: String,
    val email: String,
    val status: AttendanceStatus,
    val respondedAt: LocalDateTime?
)

data class AgendaItem(
    val id: Int,
    val title: String,
    val description: String?,
    val estimatedDuration: Int, // minutos
    val order: Int,
    val assignedToId: Int?,
    val tarjetaId: Int? = null // Relacionado con una tarjeta específica
)

enum class MeetingStatus {
    SCHEDULED,
    IN_PROGRESS,
    COMPLETED,
    CANCELED,
    POSTPONED
}

enum class AttendanceStatus {
    PENDING,
    ACCEPTED,
    DECLINED,
    TENTATIVE
}

data class TeamChat(
    val id: Int,
    val teamId: Int,
    val name: String,
    val description: String?,
    val isPrivate: Boolean,
    val createdById: Int,
    val createdAt: LocalDateTime,
    val lastMessageAt: LocalDateTime?,
    val participants: List<TeamMember>,
    val unreadCount: Int
)

data class ChatMessage(
    val id: Int,
    val chatId: Int,
    val senderId: Int,
    val senderName: String,
    val content: String,
    val messageType: MessageType,
    val timestamp: LocalDateTime,
    val editedAt: LocalDateTime?,
    val replyToId: Int?,
    val attachments: List<MessageAttachment>,
    val mentions: List<Int>, // User IDs mencionados
    val isRead: Boolean = false
)

enum class MessageType {
    TEXT,
    IMAGE,
    FILE,
    TARJETA_REFERENCE,
    SYSTEM_MESSAGE
}

data class MessageAttachment(
    val id: Int,
    val fileName: String,
    val fileUrl: String,
    val fileType: String,
    val fileSize: Long,
    val thumbnailUrl: String?
)