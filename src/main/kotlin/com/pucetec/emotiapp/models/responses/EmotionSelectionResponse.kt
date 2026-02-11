package com.pucetec.emotiapp.models.responses

import java.time.LocalDateTime

data class EmotionSelectionResponse(
    val id: Long,
    val username: String,
    val emotionId: Long,
    val emotionName: String,
    val recommendationId: Long?,
    val selectedAt: LocalDateTime,
    val recommendation: RecommendationData? = null
)

data class RecommendationData(
    val id: Long,
    val type: String,
    val content: String,
    val durationMinutes: Int?
)