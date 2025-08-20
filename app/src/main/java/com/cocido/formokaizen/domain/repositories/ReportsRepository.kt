package com.cocido.formokaizen.domain.repositories

import com.cocido.formokaizen.domain.entities.*
import com.cocido.formokaizen.utils.Resource
import kotlinx.coroutines.flow.Flow

interface ReportsRepository {
    suspend fun generateReport(request: CreateReportRequest): Flow<Resource<Report>>
    
    suspend fun getReports(page: Int = 0, pageSize: Int = 20): Flow<Resource<List<Report>>>
    
    suspend fun getReportById(reportId: Int): Flow<Resource<Report>>
    
    suspend fun downloadReport(reportId: Int): Flow<Resource<ByteArray>>
    
    suspend fun deleteReport(reportId: Int): Flow<Resource<Unit>>
    
    suspend fun getReportTemplates(): Flow<Resource<List<ReportTemplate>>>
    
    suspend fun saveReportTemplate(
        name: String,
        description: String,
        type: ReportType,
        parameters: ReportParameters
    ): Flow<Resource<ReportTemplate>>
    
    suspend fun getScheduledReports(): Flow<Resource<List<Report>>>
    
    suspend fun scheduleReport(request: CreateReportRequest): Flow<Resource<Report>>
    
    suspend fun cancelScheduledReport(reportId: Int): Flow<Resource<Unit>>
    
    suspend fun shareReport(reportId: Int, emails: List<String>): Flow<Resource<Unit>>
}