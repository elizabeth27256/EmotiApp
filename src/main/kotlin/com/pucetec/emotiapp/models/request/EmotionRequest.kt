package com.pucetec.emotiapp.models.request

data class EmotionRequest(
    val name: String,
    val description: String,
    val level: Int
)
