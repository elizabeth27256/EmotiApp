package com.pucetec.emotiapp.models.request

import com.pucetec.emotiapp.models.entities.RecommendationType

data class RecommendationRequest(
    val type: RecommendationType,
    val content: String,
    val durationMinutes: Int?
)