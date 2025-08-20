package com.cocido.formokaizen.domain.entities

import java.time.LocalDateTime

data class Report(
    val id: Int,
    val name: String,
    val description: String,
    val type: ReportType,
    val format: ReportFormat,
    val parameters: ReportParameters,
    val createdAt: LocalDateTime,
    val createdById: Int,
    val createdByName: String,
    val fileUrl: String?,
    val fileSize: Long?,
    val status: ReportStatus
)

enum class ReportType {
    TARJETAS_SUMMARY,
    TARJETAS_DETAILED,
    CATEGORY_ANALYSIS,
    USER_PERFORMANCE,
    TIMELINE_ANALYSIS,
    PRIORITY_DISTRIBUTION,
    RESOLUTION_TIME,
    DEPARTMENT_METRICS,
    CUSTOM
}

enum class ReportFormat {
    PDF,
    EXCEL,
    CSV,
    JSON
}

enum class ReportStatus {
    PENDING,
    GENERATING,
    COMPLETED,
    FAILED,
    EXPIRED
}

data class ReportParameters(
    val dateFrom: LocalDateTime? = null,
    val dateTo: LocalDateTime? = null,
    val categories: List<Int> = emptyList(),
    val departments: List<String> = emptyList(),
    val userIds: List<Int> = emptyList(),
    val status: List<TarjetaStatus> = emptyList(),
    val priority: List<TarjetaPriority> = emptyList(),
    val includeComments: Boolean = false,
    val includeHistory: Boolean = false,
    val includeAttachments: Boolean = false,
    val groupBy: ReportGroupBy? = null,
    val chartTypes: List<ChartType> = emptyList()
)

enum class ReportGroupBy {
    CATEGORY,
    DEPARTMENT,
    USER,
    PRIORITY,
    STATUS,
    MONTH,
    WEEK
}

enum class ChartType {
    BAR_CHART,
    PIE_CHART,
    LINE_CHART,
    TIMELINE,
    HEATMAP
}

data class CreateReportRequest(
    val name: String,
    val description: String,
    val type: ReportType,
    val format: ReportFormat,
    val parameters: ReportParameters,
    val scheduleType: ScheduleType? = null,
    val scheduleConfig: ScheduleConfig? = null
)

enum class ScheduleType {
    DAILY,
    WEEKLY,
    MONTHLY,
    QUARTERLY
}

data class ScheduleConfig(
    val frequency: ScheduleType,
    val dayOfWeek: Int? = null, // Para weekly (1-7)
    val dayOfMonth: Int? = null, // Para monthly (1-31)
    val time: String, // HH:mm format
    val recipients: List<String> = emptyList(), // emails
    val isActive: Boolean = true
)

data class ReportTemplate(
    val id: Int,
    val name: String,
    val description: String,
    val type: ReportType,
    val defaultParameters: ReportParameters,
    val isPublic: Boolean,
    val createdById: Int,
    val usageCount: Int
)