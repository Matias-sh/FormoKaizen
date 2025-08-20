package com.cocido.formokaizen.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ApiResponse<T>(
    val success: Boolean,
    val data: T? = null,
    val message: String? = null,
    val error: String? = null,
    @SerializedName("error_code")
    val errorCode: String? = null
)

data class PaginatedApiResponse<T>(
    val success: Boolean,
    val data: PaginatedData<T>? = null,
    val message: String? = null,
    val error: String? = null
)

data class PaginatedData<T>(
    val results: List<T>,
    val count: Int,
    val page: Int,
    @SerializedName("per_page")
    val perPage: Int,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("has_next")
    val hasNext: Boolean,
    @SerializedName("has_previous")
    val hasPrevious: Boolean
)