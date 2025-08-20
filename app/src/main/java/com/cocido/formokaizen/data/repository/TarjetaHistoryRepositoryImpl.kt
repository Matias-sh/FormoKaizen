package com.cocido.formokaizen.data.repository

import com.cocido.formokaizen.domain.entities.*
import com.cocido.formokaizen.domain.repositories.TarjetaHistoryRepository
import com.cocido.formokaizen.utils.Resource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TarjetaHistoryRepositoryImpl @Inject constructor() : TarjetaHistoryRepository {

    override suspend fun getTarjetaHistory(tarjetaId: Int, page: Int, size: Int): Flow<Resource<List<TarjetaHistory>>> {
        return try {
            delay(300)
            val history = generateMockHistory(tarjetaId)
            flowOf(Resource.Success(history))
        } catch (e: Exception) {
            flowOf(Resource.Error("Error al cargar historial: ${e.message}"))
        }
    }

    override suspend fun getTarjetaActivityFeed(tarjetaId: Int, page: Int, size: Int): Flow<Resource<TarjetaActivityFeed>> {
        return try {
            delay(400)
            val history = generateMockHistory(tarjetaId)
            val comments = generateMockCommentsForHistory(tarjetaId)
            
            val activityFeed = TarjetaActivityFeed(
                tarjetaId = tarjetaId,
                activities = history,
                comments = comments,
                totalItems = history.size + comments.size,
                hasMore = false
            )
            flowOf(Resource.Success(activityFeed))
        } catch (e: Exception) {
            flowOf(Resource.Error("Error al cargar actividad: ${e.message}"))
        }
    }

    override suspend fun getMyActivityFeed(page: Int, size: Int): Flow<Resource<List<TarjetaHistory>>> {
        return try {
            delay(300)
            val myActivities = generateMyActivityFeed()
            flowOf(Resource.Success(myActivities))
        } catch (e: Exception) {
            flowOf(Resource.Error("Error al cargar mi actividad: ${e.message}"))
        }
    }

    override suspend fun getTeamActivityFeed(page: Int, size: Int): Flow<Resource<List<TarjetaHistory>>> {
        return try {
            delay(300)
            val teamActivities = generateTeamActivityFeed()
            flowOf(Resource.Success(teamActivities))
        } catch (e: Exception) {
            flowOf(Resource.Error("Error al cargar actividad del equipo: ${e.message}"))
        }
    }

    private fun generateMockHistory(tarjetaId: Int): List<TarjetaHistory> {
        return listOf(
            TarjetaHistory(
                id = 1,
                tarjetaId = tarjetaId,
                userId = 1,
                userName = "Juan Pérez",
                action = TarjetaAction.CREATED,
                field = null,
                oldValue = null,
                newValue = null,
                timestamp = LocalDateTime.now().minusDays(5),
                description = "Tarjeta creada"
            ),
            TarjetaHistory(
                id = 2,
                tarjetaId = tarjetaId,
                userId = 2,
                userName = "María González",
                action = TarjetaAction.STATUS_CHANGED,
                field = "status",
                oldValue = "OPEN",
                newValue = "IN_PROGRESS",
                timestamp = LocalDateTime.now().minusDays(4),
                description = "Estado cambiado de Abierta a En Progreso"
            ),
            TarjetaHistory(
                id = 3,
                tarjetaId = tarjetaId,
                userId = 3,
                userName = "Carlos Rodríguez",
                action = TarjetaAction.ASSIGNED,
                field = "assignedTo",
                oldValue = null,
                newValue = "María González",
                timestamp = LocalDateTime.now().minusDays(3),
                description = "Asignada a María González"
            ),
            TarjetaHistory(
                id = 4,
                tarjetaId = tarjetaId,
                userId = 2,
                userName = "María González",
                action = TarjetaAction.PRIORITY_CHANGED,
                field = "priority",
                oldValue = "MEDIUM",
                newValue = "HIGH",
                timestamp = LocalDateTime.now().minusDays(2),
                description = "Prioridad cambiada de Media a Alta"
            ),
            TarjetaHistory(
                id = 5,
                tarjetaId = tarjetaId,
                userId = 2,
                userName = "María González",
                action = TarjetaAction.COMMENTED,
                field = null,
                oldValue = null,
                newValue = null,
                timestamp = LocalDateTime.now().minusDays(1),
                description = "Agregó un comentario"
            )
        )
    }

    private fun generateMockCommentsForHistory(tarjetaId: Int): List<TarjetaComment> {
        return listOf(
            TarjetaComment(
                id = 1,
                comment = "Comenzando trabajo en esta tarjeta.",
                isInternal = false,
                user = User(id = 2, username = "mgonzalez", email = "maria.gonzalez@empresa.com", firstName = "María", lastName = "González", role = UserRole.USER, employeeId = null, phone = null, department = null, position = null, avatar = null, isActive = true, createdAt = ""),
                createdAt = LocalDateTime.now().minusDays(1).toString()
            )
        )
    }

    private fun generateMyActivityFeed(): List<TarjetaHistory> {
        return listOf(
            TarjetaHistory(
                id = 10,
                tarjetaId = 1,
                userId = 1, // Usuario actual
                userName = "Usuario Actual",
                action = TarjetaAction.CREATED,
                field = null,
                oldValue = null,
                newValue = null,
                timestamp = LocalDateTime.now().minusHours(2),
                description = "Creaste una nueva tarjeta roja"
            ),
            TarjetaHistory(
                id = 11,
                tarjetaId = 2,
                userId = 1,
                userName = "Usuario Actual",
                action = TarjetaAction.COMMENTED,
                field = null,
                oldValue = null,
                newValue = null,
                timestamp = LocalDateTime.now().minusHours(4),
                description = "Comentaste en la tarjeta #TR-002"
            )
        )
    }

    private fun generateTeamActivityFeed(): List<TarjetaHistory> {
        return listOf(
            TarjetaHistory(
                id = 20,
                tarjetaId = 3,
                userId = 2,
                userName = "María González",
                action = TarjetaAction.RESOLVED,
                field = "status",
                oldValue = "IN_PROGRESS",
                newValue = "RESOLVED",
                timestamp = LocalDateTime.now().minusMinutes(30),
                description = "Resolvió la tarjeta #TR-003"
            ),
            TarjetaHistory(
                id = 21,
                tarjetaId = 4,
                userId = 3,
                userName = "Carlos Rodríguez",
                action = TarjetaAction.ASSIGNED,
                field = "assignedTo",
                oldValue = null,
                newValue = "Ana López",
                timestamp = LocalDateTime.now().minusHours(1),
                description = "Asignó la tarjeta #TR-004 a Ana López"
            ),
            TarjetaHistory(
                id = 22,
                tarjetaId = 5,
                userId = 4,
                userName = "Ana López",
                action = TarjetaAction.PRIORITY_CHANGED,
                field = "priority",
                oldValue = "LOW",
                newValue = "MEDIUM",
                timestamp = LocalDateTime.now().minusHours(3),
                description = "Cambió la prioridad de la tarjeta #TR-005"
            )
        )
    }
}