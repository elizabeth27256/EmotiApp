package com.pucetec.emotiapp.mappers

import com.pucetec.emotiapp.models.entities.Emotion
import com.pucetec.emotiapp.models.request.EmotionRequest
import com.pucetec.emotiapp.models.responses.EmotionResponse
import org.springframework.stereotype.Component

@Component
class EmotionMapper {

    fun toEntity(request: EmotionRequest): Emotion {
        return Emotion(
            name = request.name,
            description = request.description,
            level = request.level
        )
    }

    fun toResponse(emotion: Emotion): EmotionResponse {
        return EmotionResponse(
            id = emotion.id,
            name = emotion.name,
            description = emotion.description,
            level = emotion.level
        )
    }
}