package com.cocido.formokaizen.domain.entities

import java.time.LocalDateTime

data class Comment(
    val id: Int,
    val tarjetaId: Int,
    val authorId: Int,
    val authorName: String,
    val authorAvatar: String?,
    val content: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime?,
    val isEdited: Boolean = false,
    val attachments: List<CommentAttachment> = emptyList()
)

data class CommentAttachment(
    val id: Int,
    val commentId: Int,
    val fileName: String,
    val fileUrl: String,
    val fileType: String,
    val fileSize: Long
)

data class CreateCommentRequest(
    val tarjetaId: Int,
    val content: String,
    val attachments: List<String> = emptyList()
)

data class UpdateCommentRequest(
    val content: String,
    val attachments: List<String> = emptyList()
)