package com.cocido.formokaizen.domain.repositories

import com.cocido.formokaizen.domain.entities.Comment
import com.cocido.formokaizen.domain.entities.CreateCommentRequest
import com.cocido.formokaizen.domain.entities.UpdateCommentRequest
import com.cocido.formokaizen.utils.Resource
import kotlinx.coroutines.flow.Flow

interface CommentsRepository {
    suspend fun getCommentsByTarjeta(tarjetaId: Int): Flow<Resource<List<Comment>>>
    suspend fun createComment(request: CreateCommentRequest): Flow<Resource<Comment>>
    suspend fun updateComment(commentId: Int, request: UpdateCommentRequest): Flow<Resource<Comment>>
    suspend fun deleteComment(commentId: Int): Flow<Resource<Unit>>
    suspend fun likeComment(commentId: Int): Flow<Resource<Unit>>
    suspend fun unlikeComment(commentId: Int): Flow<Resource<Unit>>
}