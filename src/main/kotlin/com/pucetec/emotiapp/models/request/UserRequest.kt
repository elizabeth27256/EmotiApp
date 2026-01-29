package com.pucetec.emotiapp.models.request

data class UserRequest(
    val userIdentifier: String,
    val emotionId: Long,
    val recommendationId: Long?
)