package com.cocido.formokaizen.domain.entities

data class UserSettings(
    val userId: Int,
    val notifications: NotificationSettings,
    val display: DisplaySettings,
    val privacy: PrivacySettings,
    val workflow: WorkflowSettings,
    val language: String = "es",
    val timezone: String = "America/Argentina/Buenos_Aires"
)

data class NotificationSettings(
    val pushNotifications: Boolean = true,
    val emailNotifications: Boolean = true,
    val assignmentNotifications: Boolean = true,
    val commentNotifications: Boolean = true,
    val statusChangeNotifications: Boolean = true,
    val dueDateReminders: Boolean = true,
    val reminderTimeBefore: Int = 24, // horas antes
    val quietHoursEnabled: Boolean = false,
    val quietHoursStart: String = "22:00",
    val quietHoursEnd: String = "08:00",
    val weekendNotifications: Boolean = false
)

data class DisplaySettings(
    val theme: AppTheme = AppTheme.SYSTEM,
    val colorScheme: ColorScheme = ColorScheme.DEFAULT,
    val fontSize: FontSize = FontSize.MEDIUM,
    val compactView: Boolean = false,
    val showImages: Boolean = true,
    val showAvatars: Boolean = true,
    val animationsEnabled: Boolean = true,
    val defaultView: DefaultView = DefaultView.GRID
)

data class PrivacySettings(
    val showOnlineStatus: Boolean = true,
    val showLastSeen: Boolean = true,
    val allowDirectMessages: Boolean = true,
    val showInTeamDirectory: Boolean = true,
    val shareWorkload: Boolean = true,
    val allowMentions: Boolean = true
)

data class WorkflowSettings(
    val defaultPriority: TarjetaPriority = TarjetaPriority.MEDIUM,
    val autoAssignToMe: Boolean = false,
    val defaultEstimatedDays: Int = 7,
    val requireDescriptionMinLength: Int = 20,
    val enableQuickActions: Boolean = true,
    val defaultFilters: SearchFilter? = null,
    val favoriteDashboards: List<String> = emptyList(),
    val customStatuses: List<CustomStatus> = emptyList()
)

enum class AppTheme {
    LIGHT,
    DARK,
    SYSTEM
}

enum class ColorScheme {
    DEFAULT,
    BLUE,
    GREEN,
    PURPLE,
    ORANGE
}

enum class FontSize {
    SMALL,
    MEDIUM,
    LARGE,
    EXTRA_LARGE
}

enum class DefaultView {
    LIST,
    GRID,
    KANBAN
}

data class CustomStatus(
    val id: String,
    val name: String,
    val color: String,
    val icon: String?,
    val description: String?
)

data class UpdateSettingsRequest(
    val notifications: NotificationSettings? = null,
    val display: DisplaySettings? = null,
    val privacy: PrivacySettings? = null,
    val workflow: WorkflowSettings? = null,
    val language: String? = null,
    val timezone: String? = null
)