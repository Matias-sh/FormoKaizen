package com.cocido.formokaizen.data.repository

import com.cocido.formokaizen.domain.entities.*
import com.cocido.formokaizen.domain.repositories.UserSettingsRepository
import com.cocido.formokaizen.utils.Resource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserSettingsRepositoryImpl @Inject constructor() : UserSettingsRepository {

    override suspend fun getUserSettings(): Flow<Resource<UserSettings>> {
        return try {
            delay(200)
            val settings = getDefaultUserSettings()
            flowOf(Resource.Success(settings))
        } catch (e: Exception) {
            flowOf(Resource.Error("Error al cargar configuraciones: ${e.message}"))
        }
    }

    override suspend fun updateUserSettings(request: UpdateSettingsRequest): Flow<Resource<UserSettings>> {
        return try {
            delay(300)
            // Aquí normalmente harías merge con las configuraciones existentes
            val updatedSettings = getDefaultUserSettings().copy(
                notifications = request.notifications ?: getDefaultUserSettings().notifications,
                display = request.display ?: getDefaultUserSettings().display,
                privacy = request.privacy ?: getDefaultUserSettings().privacy,
                workflow = request.workflow ?: getDefaultUserSettings().workflow,
                language = request.language ?: getDefaultUserSettings().language,
                timezone = request.timezone ?: getDefaultUserSettings().timezone
            )
            flowOf(Resource.Success(updatedSettings))
        } catch (e: Exception) {
            flowOf(Resource.Error("Error al actualizar configuraciones: ${e.message}"))
        }
    }

    override suspend fun resetToDefaults(): Flow<Resource<UserSettings>> {
        return try {
            delay(200)
            val defaultSettings = getDefaultUserSettings()
            flowOf(Resource.Success(defaultSettings))
        } catch (e: Exception) {
            flowOf(Resource.Error("Error al restablecer configuraciones: ${e.message}"))
        }
    }

    override suspend fun exportSettings(): Flow<Resource<String>> {
        return try {
            delay(300)
            val settings = getDefaultUserSettings()
            // Aquí normalmente convertirías a JSON
            val exportJson = """
                {
                    "notifications": {
                        "pushNotifications": ${settings.notifications.pushNotifications},
                        "emailNotifications": ${settings.notifications.emailNotifications},
                        "assignmentNotifications": ${settings.notifications.assignmentNotifications}
                    },
                    "display": {
                        "theme": "${settings.display.theme}",
                        "colorScheme": "${settings.display.colorScheme}",
                        "fontSize": "${settings.display.fontSize}"
                    },
                    "language": "${settings.language}",
                    "timezone": "${settings.timezone}"
                }
            """.trimIndent()
            flowOf(Resource.Success(exportJson))
        } catch (e: Exception) {
            flowOf(Resource.Error("Error al exportar configuraciones: ${e.message}"))
        }
    }

    override suspend fun importSettings(settingsJson: String): Flow<Resource<UserSettings>> {
        return try {
            delay(400)
            // Aquí normalmente parsearías el JSON y validarías
            val importedSettings = getDefaultUserSettings()
            flowOf(Resource.Success(importedSettings))
        } catch (e: Exception) {
            flowOf(Resource.Error("Error al importar configuraciones: ${e.message}"))
        }
    }

    private fun getDefaultUserSettings(): UserSettings {
        return UserSettings(
            userId = 1,
            notifications = NotificationSettings(
                pushNotifications = true,
                emailNotifications = true,
                assignmentNotifications = true,
                commentNotifications = true,
                statusChangeNotifications = true,
                dueDateReminders = true,
                reminderTimeBefore = 24,
                quietHoursEnabled = false,
                quietHoursStart = "22:00",
                quietHoursEnd = "08:00",
                weekendNotifications = false
            ),
            display = DisplaySettings(
                theme = AppTheme.SYSTEM,
                colorScheme = ColorScheme.DEFAULT,
                fontSize = FontSize.MEDIUM,
                compactView = false,
                showImages = true,
                showAvatars = true,
                animationsEnabled = true,
                defaultView = DefaultView.GRID
            ),
            privacy = PrivacySettings(
                showOnlineStatus = true,
                showLastSeen = true,
                allowDirectMessages = true,
                showInTeamDirectory = true,
                shareWorkload = true,
                allowMentions = true
            ),
            workflow = WorkflowSettings(
                defaultPriority = TarjetaPriority.MEDIUM,
                autoAssignToMe = false,
                defaultEstimatedDays = 7,
                requireDescriptionMinLength = 20,
                enableQuickActions = true,
                defaultFilters = null,
                favoriteDashboards = listOf("dashboard_main", "dashboard_my_tasks"),
                customStatuses = listOf(
                    CustomStatus(
                        id = "waiting_parts",
                        name = "Esperando Repuestos",
                        color = "#FFA500",
                        icon = "schedule",
                        description = "Esperando llegada de repuestos para continuar"
                    ),
                    CustomStatus(
                        id = "approval_pending",
                        name = "Pendiente Aprobación",
                        color = "#9C27B0",
                        icon = "approval",
                        description = "Esperando aprobación de supervisor"
                    )
                )
            ),
            language = "es",
            timezone = "America/Argentina/Buenos_Aires"
        )
    }
}