package com.cocido.formokaizen.data.repository

import com.cocido.formokaizen.domain.entities.*
import com.cocido.formokaizen.domain.repositories.SearchRepository
import com.cocido.formokaizen.utils.Resource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchRepositoryImpl @Inject constructor() : SearchRepository {

    override suspend fun searchTarjetas(filter: SearchFilter, page: Int, pageSize: Int): Flow<Resource<SearchResult>> {
        return try {
            delay(400)
            val mockTarjetas = generateFilteredMockTarjetas(filter)
            val result = SearchResult(
                tarjetas = mockTarjetas,
                totalCount = mockTarjetas.size,
                hasMore = false,
                page = page,
                pageSize = pageSize
            )
            flowOf(Resource.Success(result))
        } catch (e: Exception) {
            flowOf(Resource.Error("Error en búsqueda: ${e.message}"))
        }
    }

    override suspend fun getQuickSearchSuggestions(query: String): Flow<Resource<List<String>>> {
        return try {
            delay(200)
            val suggestions = listOf(
                "Problema de calidad",
                "Seguridad industrial",
                "Proceso de producción",
                "Mantenimiento preventivo",
                "Capacitación personal",
                "Mejora continua",
                "Control de inventario",
                "Gestión de residuos"
            ).filter { it.contains(query, ignoreCase = true) }
            flowOf(Resource.Success(suggestions))
        } catch (e: Exception) {
            flowOf(Resource.Error("Error al cargar sugerencias: ${e.message}"))
        }
    }

    override suspend fun getFilterOptions(): Flow<Resource<List<FilterGroup>>> {
        return try {
            delay(300)
            val filterGroups = listOf(
                FilterGroup(
                    title = "Estado",
                    key = "status",
                    options = listOf(
                        FilterOption("OPEN", "Abierta", 15),
                        FilterOption("IN_PROGRESS", "En Progreso", 8),
                        FilterOption("RESOLVED", "Resuelta", 25),
                        FilterOption("CLOSED", "Cerrada", 12)
                    )
                ),
                FilterGroup(
                    title = "Prioridad",
                    key = "priority",
                    options = listOf(
                        FilterOption("HIGH", "Alta", 5),
                        FilterOption("MEDIUM", "Media", 20),
                        FilterOption("LOW", "Baja", 35)
                    )
                ),
                FilterGroup(
                    title = "Categoría",
                    key = "category",
                    options = listOf(
                        FilterOption("1", "Calidad", 18),
                        FilterOption("2", "Seguridad", 12),
                        FilterOption("3", "Proceso", 15),
                        FilterOption("4", "Mantenimiento", 10),
                        FilterOption("5", "Otros", 5)
                    )
                ),
                FilterGroup(
                    title = "Asignado a",
                    key = "assignedTo",
                    options = listOf(
                        FilterOption("1", "María González", 8),
                        FilterOption("2", "Carlos Pérez", 6),
                        FilterOption("3", "Ana López", 4),
                        FilterOption("4", "Luis Martín", 7),
                        FilterOption("0", "Sin asignar", 15)
                    )
                )
            )
            flowOf(Resource.Success(filterGroups))
        } catch (e: Exception) {
            flowOf(Resource.Error("Error al cargar opciones de filtro: ${e.message}"))
        }
    }

    override suspend fun saveFilter(name: String, description: String?, filter: SearchFilter): Flow<Resource<SavedFilter>> {
        return try {
            delay(200)
            val savedFilter = SavedFilter(
                id = (1..1000).random(),
                name = name,
                description = description,
                filter = filter,
                isPublic = false,
                createdById = 1,
                createdAt = LocalDateTime.now(),
                usageCount = 0
            )
            flowOf(Resource.Success(savedFilter))
        } catch (e: Exception) {
            flowOf(Resource.Error("Error al guardar filtro: ${e.message}"))
        }
    }

    override suspend fun getSavedFilters(): Flow<Resource<List<SavedFilter>>> {
        return try {
            delay(300)
            val savedFilters = listOf(
                SavedFilter(
                    id = 1,
                    name = "Mis tarjetas pendientes",
                    description = "Tarjetas asignadas a mí que están abiertas o en progreso",
                    filter = SearchFilter(
                        status = listOf(TarjetaStatus.OPEN, TarjetaStatus.IN_PROGRESS),
                        assignedToIds = listOf(1)
                    ),
                    isPublic = false,
                    createdById = 1,
                    createdAt = LocalDateTime.now().minusDays(7),
                    usageCount = 15
                ),
                SavedFilter(
                    id = 2,
                    name = "Alta prioridad",
                    description = "Todas las tarjetas de alta prioridad",
                    filter = SearchFilter(
                        priority = listOf(TarjetaPriority.HIGH)
                    ),
                    isPublic = true,
                    createdById = 1,
                    createdAt = LocalDateTime.now().minusDays(3),
                    usageCount = 8
                )
            )
            flowOf(Resource.Success(savedFilters))
        } catch (e: Exception) {
            flowOf(Resource.Error("Error al cargar filtros guardados: ${e.message}"))
        }
    }

    override suspend fun deleteSavedFilter(filterId: Int): Flow<Resource<Unit>> {
        return try {
            delay(100)
            flowOf(Resource.Success(Unit))
        } catch (e: Exception) {
            flowOf(Resource.Error("Error al eliminar filtro: ${e.message}"))
        }
    }

    override suspend fun getPopularTags(): Flow<Resource<List<String>>> {
        return try {
            delay(200)
            val tags = listOf(
                "urgente", "calidad", "seguridad", "proceso", "mantenimiento",
                "capacitación", "mejora-continua", "5s", "kaizen", "lean"
            )
            flowOf(Resource.Success(tags))
        } catch (e: Exception) {
            flowOf(Resource.Error("Error al cargar tags populares: ${e.message}"))
        }
    }

    override suspend fun getRecentSearches(): Flow<Resource<List<String>>> {
        return try {
            delay(150)
            val recentSearches = listOf(
                "problema calidad línea 2",
                "seguridad industrial",
                "mantenimiento preventivo",
                "capacitación operarios",
                "mejora proceso"
            )
            flowOf(Resource.Success(recentSearches))
        } catch (e: Exception) {
            flowOf(Resource.Error("Error al cargar búsquedas recientes: ${e.message}"))
        }
    }

    override suspend fun saveSearch(query: String): Flow<Resource<Unit>> {
        return try {
            delay(50)
            flowOf(Resource.Success(Unit))
        } catch (e: Exception) {
            flowOf(Resource.Error("Error al guardar búsqueda: ${e.message}"))
        }
    }

    private fun generateFilteredMockTarjetas(filter: SearchFilter): List<TarjetaRoja> {
        val baseTarjetas = listOf(
            TarjetaRoja(
                id = 1,
                numero = "TR-001",
                fecha = "2024-01-15",
                sector = "Producción",
                descripcion = "Se detectaron productos defectuosos",
                motivo = "Defecto de calidad",
                quienLoHizo = "Juan Pérez",
                destinoFinal = "Corrección del proceso",
                fechaFinal = null,
                createdBy = User(id = 1, username = "jperez", email = "juan.perez@empresa.com", firstName = "Juan", lastName = "Pérez", role = UserRole.USER, employeeId = null, phone = null, department = null, position = null, avatar = null, isActive = true, createdAt = ""),
                assignedTo = User(id = 2, username = "mgonzalez", email = "maria.gonzalez@empresa.com", firstName = "María", lastName = "González", role = UserRole.USER, employeeId = null, phone = null, department = null, position = null, avatar = null, isActive = true, createdAt = ""),
                approvedBy = null,
                status = TarjetaStatus.OPEN,
                priority = TarjetaPriority.HIGH,
                images = emptyList(),
                comments = emptyList(),
                history = emptyList(),
                createdAt = LocalDateTime.now().minusDays(2).toString(),
                updatedAt = LocalDateTime.now().minusDays(1).toString(),
                approvedAt = null,
                closedAt = null,
                isOverdue = false,
                daysOpen = 2
            ),
            TarjetaRoja(
                id = 2,
                numero = "TR-002",
                fecha = "2024-01-16",
                sector = "Recursos Humanos",
                descripcion = "Los nuevos empleados necesitan capacitación",
                motivo = "Capacitación",
                quienLoHizo = "María González",
                destinoFinal = "Personal capacitado",
                fechaFinal = null,
                createdBy = User(id = 2, username = "mgonzalez", email = "maria.gonzalez@empresa.com", firstName = "María", lastName = "González", role = UserRole.USER, employeeId = null, phone = null, department = null, position = null, avatar = null, isActive = true, createdAt = ""),
                assignedTo = User(id = 3, username = "cperez", email = "carlos.perez@empresa.com", firstName = "Carlos", lastName = "Pérez", role = UserRole.USER, employeeId = null, phone = null, department = null, position = null, avatar = null, isActive = true, createdAt = ""),
                approvedBy = null,
                status = TarjetaStatus.IN_PROGRESS,
                priority = TarjetaPriority.MEDIUM,
                images = emptyList(),
                comments = emptyList(),
                history = emptyList(),
                createdAt = LocalDateTime.now().minusDays(5).toString(),
                updatedAt = LocalDateTime.now().minusHours(2).toString(),
                approvedAt = null,
                closedAt = null,
                isOverdue = false,
                daysOpen = 5
            )
        )

        // Aplicar filtros
        return baseTarjetas.filter { tarjeta ->
            val matchesQuery = filter.query?.let { query ->
                tarjeta.numero.contains(query, ignoreCase = true) ||
                tarjeta.descripcion.contains(query, ignoreCase = true) ||
                tarjeta.sector.contains(query, ignoreCase = true)
            } ?: true

            val matchesStatus = filter.status.isEmpty() || filter.status.contains(tarjeta.status)
            val matchesPriority = filter.priority.isEmpty() || filter.priority.contains(tarjeta.priority)
            val matchesAssignedTo = filter.assignedToIds.isEmpty() || 
                (tarjeta.assignedTo != null && filter.assignedToIds.contains(tarjeta.assignedTo.id))

            matchesQuery && matchesStatus && matchesPriority && matchesAssignedTo
        }
    }
}