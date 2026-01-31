package com.pucetec.emotiapp.models.request

import com.pucetec.emotiapp.models.entities.RecommendationType

data class EmotionSelectionRequest(
    val emotionId: Long,
    val type: RecommendationType
)