package com.cocido.formokaizen.domain.repositories

import com.cocido.formokaizen.domain.entities.*
import com.cocido.formokaizen.utils.Resource
import kotlinx.coroutines.flow.Flow

interface TeamRepository {
    // Gestión de equipos
    suspend fun getMyTeams(): Flow<Resource<List<Team>>>
    suspend fun getTeamById(teamId: Int): Flow<Resource<Team>>
    suspend fun createTeam(name: String, description: String?, departmentId: Int): Flow<Resource<Team>>
    suspend fun updateTeam(teamId: Int, name: String, description: String?): Flow<Resource<Team>>
    suspend fun deleteTeam(teamId: Int): Flow<Resource<Unit>>
    
    // Gestión de miembros
    suspend fun getTeamMembers(teamId: Int): Flow<Resource<List<TeamMember>>>
    suspend fun inviteTeamMember(teamId: Int, email: String, role: TeamRole, message: String?): Flow<Resource<TeamInvitation>>
    suspend fun removeMemberFromTeam(teamId: Int, userId: Int): Flow<Resource<Unit>>
    suspend fun updateMemberRole(teamId: Int, userId: Int, role: TeamRole): Flow<Resource<Unit>>
    
    // Invitaciones
    suspend fun getMyInvitations(): Flow<Resource<List<TeamInvitation>>>
    suspend fun respondToInvitation(invitationId: Int, accept: Boolean): Flow<Resource<Unit>>
    suspend fun cancelInvitation(invitationId: Int): Flow<Resource<Unit>>
    
    // Actividad del equipo
    suspend fun getTeamActivity(teamId: Int, page: Int = 0, size: Int = 20): Flow<Resource<List<TeamActivity>>>
    suspend fun getTeamStats(teamId: Int): Flow<Resource<TeamStats>>
    
    // Reuniones
    suspend fun getTeamMeetings(teamId: Int): Flow<Resource<List<TeamMeeting>>>
    suspend fun scheduleMeeting(
        teamId: Int,
        title: String,
        description: String?,
        scheduledDate: java.time.LocalDateTime,
        duration: Int,
        location: String?,
        attendeeIds: List<Int>,
        agenda: List<AgendaItem>
    ): Flow<Resource<TeamMeeting>>
    suspend fun updateMeetingAttendance(meetingId: Int, status: AttendanceStatus): Flow<Resource<Unit>>
    suspend fun cancelMeeting(meetingId: Int): Flow<Resource<Unit>>
    
    // Chat del equipo
    suspend fun getTeamChats(teamId: Int): Flow<Resource<List<TeamChat>>>
    suspend fun createTeamChat(teamId: Int, name: String, description: String?, isPrivate: Boolean): Flow<Resource<TeamChat>>
    suspend fun getChatMessages(chatId: Int, page: Int = 0, size: Int = 50): Flow<Resource<List<ChatMessage>>>
    suspend fun sendMessage(chatId: Int, content: String, messageType: MessageType, mentions: List<Int>): Flow<Resource<ChatMessage>>
    suspend fun markMessagesAsRead(chatId: Int): Flow<Resource<Unit>>
}