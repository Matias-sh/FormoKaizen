package com.cocido.formokaizen.data.remote.api

import com.cocido.formokaizen.data.remote.dto.CategoryDto
import com.cocido.formokaizen.data.remote.dto.WorkAreaDto
import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.*

interface CategoriesApi {
    
    @GET("categories/")
    suspend fun getCategories(): Response<List<CategoryDto>>
    
    @GET("categories/{id}/")
    suspend fun getCategoryById(@Path("id") id: Int): Response<CategoryDto>
    
    @POST("categories/")
    suspend fun createCategory(@Body request: CreateCategoryRequest): Response<CategoryDto>
    
    @PUT("categories/{id}/")
    suspend fun updateCategory(
        @Path("id") id: Int,
        @Body category: UpdateCategoryRequest
    ): Response<CategoryDto>
    
    @DELETE("categories/{id}/")
    suspend fun deleteCategory(@Path("id") id: Int): Response<ApiResponse<Unit>>
    
    @GET("categories/{id}/work-areas/")
    suspend fun getWorkAreas(@Path("id") categoryId: Int): Response<List<WorkAreaDto>>
    
    @POST("categories/{id}/work-areas/")
    suspend fun createWorkArea(
        @Path("id") categoryId: Int,
        @Body request: CreateWorkAreaRequest
    ): Response<WorkAreaDto>
}

data class CreateCategoryRequest(
    val name: String,
    val description: String,
    val color: String,
    val icon: String
)

data class UpdateCategoryRequest(
    val name: String?,
    val description: String?,
    val color: String?,
    val icon: String?
)

data class CreateWorkAreaRequest(
    val name: String,
    val description: String,
    @SerializedName("responsible_id")
    val responsibleId: Int?
)