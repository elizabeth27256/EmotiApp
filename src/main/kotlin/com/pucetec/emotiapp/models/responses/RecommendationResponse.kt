package com.pucetec.emotiapp.models.responses

import com.pucetec.emotiapp.models.entities.RecommendationType

data class RecommendationResponse(
    val id: Long,
    val emotionId: Long,
    val emotionName: String,
    val type: RecommendationType,
    val content: String,
    val durationMinutes: Int?
)