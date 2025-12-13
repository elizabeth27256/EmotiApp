package com.pucetec.emotiapp.models.responses

data class EmotionResponse(
    val id: Long,
    val name: String,
    val description: String,
    val level: Int
)
