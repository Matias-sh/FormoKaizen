package com.cocido.formokaizen.domain.repository

import com.cocido.formokaizen.domain.entities.Team
// import com.cocido.formokaizen.domain.entities.TeamProject
// import com.cocido.formokaizen.domain.entities.TeamMembership
import com.cocido.formokaizen.utils.Resource
import kotlinx.coroutines.flow.Flow

interface TeamsRepository {
    suspend fun getTeams(): Flow<Resource<List<Team>>>
    suspend fun getTeamById(id: Int): Flow<Resource<Team>>
    suspend fun createTeam(team: Team): Flow<Resource<Team>>
    suspend fun updateTeam(team: Team): Flow<Resource<Team>>
    suspend fun deleteTeam(id: Int): Flow<Resource<Unit>>
    
    // suspend fun addTeamMember(teamId: Int, userId: Int, role: String): Flow<Resource<TeamMembership>>
    // suspend fun removeTeamMember(teamId: Int, membershipId: Int): Flow<Resource<Unit>>
    // suspend fun updateTeamMemberRole(teamId: Int, membershipId: Int, role: String): Flow<Resource<TeamMembership>>
    
    // suspend fun getTeamProjects(teamId: Int): Flow<Resource<List<TeamProject>>>
    // suspend fun createTeamProject(teamId: Int, project: TeamProject): Flow<Resource<TeamProject>>
    
    // Offline support
    fun getTeamsOffline(): Flow<List<Team>>
    suspend fun syncTeams(): Flow<Resource<Unit>>
}