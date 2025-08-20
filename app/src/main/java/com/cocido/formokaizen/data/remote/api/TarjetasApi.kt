package com.cocido.formokaizen.data.remote.api

import com.cocido.formokaizen.data.remote.dto.*
import com.google.gson.annotations.SerializedName
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface TarjetasApi {
    
    @GET("tarjetas/")
    suspend fun getTarjetas(
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 20,
        @Query("status") status: String? = null,
        @Query("category") category: Int? = null,
        @Query("priority") priority: String? = null,
        @Query("assigned_to") assignedTo: String? = null,
        @Query("created_by") createdBy: String? = null,
        @Query("search") search: String? = null
    ): Response<PaginatedResponse<TarjetaRojaDto>>
    
    @GET("tarjetas/{id}/")
    suspend fun getTarjetaById(@Path("id") id: Int): Response<TarjetaRojaDto>
    
    @POST("tarjetas/")
    suspend fun createTarjeta(@Body request: CreateTarjetaRequest): Response<TarjetaRojaDto>
    
    @PUT("tarjetas/{id}/")
    suspend fun updateTarjeta(
        @Path("id") id: Int,
        @Body tarjeta: UpdateTarjetaRequest
    ): Response<TarjetaRojaDto>
    
    @DELETE("tarjetas/{id}/")
    suspend fun deleteTarjeta(@Path("id") id: Int): Response<ApiResponse<Unit>>
    
    @POST("tarjetas/{id}/approve/")
    suspend fun approveTarjeta(
        @Path("id") id: Int,
        @Body request: ApproveTarjetaRequest
    ): Response<ApproveTarjetaResponse>
    
    @Multipart
    @POST("tarjetas/{id}/upload-image/")
    suspend fun uploadImage(
        @Path("id") id: Int,
        @Part image: MultipartBody.Part,
        @Part("description") description: String
    ): Response<TarjetaImageDto>
    
    @POST("tarjetas/{id}/add-comment/")
    suspend fun addComment(
        @Path("id") id: Int,
        @Body request: AddCommentRequest
    ): Response<TarjetaCommentDto>
    
    @GET("tarjetas/dashboard/stats/")
    suspend fun getDashboardStats(): Response<DashboardStatsResponse>
}

data class PaginatedResponse<T>(
    val results: List<T>,
    val count: Int,
    val page: Int,
    val per_page: Int,
    val total_pages: Int
)

data class UpdateTarjetaRequest(
    val title: String?,
    val description: String?,
    val priority: String?,
    val sector: String?,
    val motivo: String?,
    @SerializedName("destino_final")
    val destinoFinal: String?,
    @SerializedName("assigned_to_id")
    val assignedToId: Int?,
    @SerializedName("estimated_resolution_date")
    val estimatedResolutionDate: String?,
    val status: String?,
    @SerializedName("resolution_notes")
    val resolutionNotes: String?,
    @SerializedName("actual_resolution_date")
    val actualResolutionDate: String?
)

data class AddCommentRequest(
    val comment: String,
    @SerializedName("is_internal")
    val isInternal: Boolean = false
)

data class ApproveTarjetaResponse(
    val message: String,
    val tarjeta: TarjetaRojaDto
)

data class DashboardStatsResponse(
    @SerializedName("total_tarjetas")
    val totalTarjetas: Int,
    @SerializedName("status_stats")
    val statusStats: Map<String, Int>,
    @SerializedName("priority_stats")
    val priorityStats: Map<String, Int>,
    @SerializedName("category_stats")
    val categoryStats: List<CategoryStatsDto>
)

data class CategoryStatsDto(
    val name: String,
    val total: Int,
    val open: Int
)