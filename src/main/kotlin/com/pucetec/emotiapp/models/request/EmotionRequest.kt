package com.pucetec.emotiapp.models.requests

data class EmotionRequest(
    val name: String,
    val description: String,
    val level: Int
)
