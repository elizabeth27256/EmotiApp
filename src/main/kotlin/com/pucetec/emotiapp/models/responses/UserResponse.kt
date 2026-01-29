package com.pucetec.emotiapp.models.responses

import java.time.LocalDateTime

data class UserResponse(
    val id: Long,
    val userIdentifier: String,
    val emotionId: Long,
    val emotionName: String,
    val recommendationId: Long?,
    val selectedAt: LocalDateTime
)