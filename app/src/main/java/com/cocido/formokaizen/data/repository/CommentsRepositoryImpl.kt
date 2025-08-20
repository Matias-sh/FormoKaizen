package com.cocido.formokaizen.data.repository

import com.cocido.formokaizen.domain.entities.Comment
import com.cocido.formokaizen.domain.entities.CreateCommentRequest
import com.cocido.formokaizen.domain.entities.UpdateCommentRequest
import com.cocido.formokaizen.domain.repositories.CommentsRepository
import com.cocido.formokaizen.utils.Resource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CommentsRepositoryImpl @Inject constructor() : CommentsRepository {

    override suspend fun getCommentsByTarjeta(tarjetaId: Int): Flow<Resource<List<Comment>>> {
        return try {
            delay(500) // Simular llamada a API
            val comments = generateMockComments(tarjetaId)
            flowOf(Resource.Success(comments))
        } catch (e: Exception) {
            flowOf(Resource.Error("Error al cargar comentarios: ${e.message}"))
        }
    }

    override suspend fun createComment(request: CreateCommentRequest): Flow<Resource<Comment>> {
        return try {
            delay(300)
            val comment = Comment(
                id = (1..1000).random(),
                tarjetaId = request.tarjetaId,
                authorId = 1, // Usuario actual
                authorName = "Usuario Actual",
                authorAvatar = null,
                content = request.content,
                createdAt = LocalDateTime.now(),
                updatedAt = null,
                isEdited = false
            )
            flowOf(Resource.Success(comment))
        } catch (e: Exception) {
            flowOf(Resource.Error("Error al crear comentario: ${e.message}"))
        }
    }

    override suspend fun updateComment(commentId: Int, request: UpdateCommentRequest): Flow<Resource<Comment>> {
        return try {
            delay(300)
            val comment = Comment(
                id = commentId,
                tarjetaId = 1,
                authorId = 1,
                authorName = "Usuario Actual",
                authorAvatar = null,
                content = request.content,
                createdAt = LocalDateTime.now().minusHours(1),
                updatedAt = LocalDateTime.now(),
                isEdited = true
            )
            flowOf(Resource.Success(comment))
        } catch (e: Exception) {
            flowOf(Resource.Error("Error al actualizar comentario: ${e.message}"))
        }
    }

    override suspend fun deleteComment(commentId: Int): Flow<Resource<Unit>> {
        return try {
            delay(200)
            flowOf(Resource.Success(Unit))
        } catch (e: Exception) {
            flowOf(Resource.Error("Error al eliminar comentario: ${e.message}"))
        }
    }

    override suspend fun likeComment(commentId: Int): Flow<Resource<Unit>> {
        return try {
            delay(100)
            flowOf(Resource.Success(Unit))
        } catch (e: Exception) {
            flowOf(Resource.Error("Error al dar like: ${e.message}"))
        }
    }

    override suspend fun unlikeComment(commentId: Int): Flow<Resource<Unit>> {
        return try {
            delay(100)
            flowOf(Resource.Success(Unit))
        } catch (e: Exception) {
            flowOf(Resource.Error("Error al quitar like: ${e.message}"))
        }
    }

    private fun generateMockComments(tarjetaId: Int): List<Comment> {
        return listOf(
            Comment(
                id = 1,
                tarjetaId = tarjetaId,
                authorId = 2,
                authorName = "María González",
                authorAvatar = null,
                content = "Excelente identificación del problema. Sugiero implementar la solución en la próxima semana.",
                createdAt = LocalDateTime.now().minusHours(2),
                updatedAt = null,
                isEdited = false
            ),
            Comment(
                id = 2,
                tarjetaId = tarjetaId,
                authorId = 3,
                authorName = "Carlos Pérez",
                authorAvatar = null,
                content = "De acuerdo con María. ¿Podríamos coordinar una reunión para discutir los detalles de implementación?",
                createdAt = LocalDateTime.now().minusHours(1),
                updatedAt = null,
                isEdited = false
            ),
            Comment(
                id = 3,
                tarjetaId = tarjetaId,
                authorId = 1,
                authorName = "Usuario Actual",
                authorAvatar = null,
                content = "Perfecto, organizo la reunión para mañana a las 10:00 AM.",
                createdAt = LocalDateTime.now().minusMinutes(30),
                updatedAt = LocalDateTime.now().minusMinutes(15),
                isEdited = true
            )
        )
    }
}