package com.pucetec.emotiapp.mappers

import com.pucetec.emotiapp.models.entities.Emotion
import com.pucetec.emotiapp.models.entities.Recommendation
import com.pucetec.emotiapp.models.request.RecommendationRequest
import com.pucetec.emotiapp.models.responses.RecommendationResponse
import org.springframework.stereotype.Component

@Component
class RecommendationMapper {

    fun toEntity(request: RecommendationRequest, emotion: Emotion): Recommendation {
        return Recommendation(
            emotion = emotion,
            type = request.type,
            content = request.content,
            durationMinutes = request.durationMinutes
        )
    }

    fun toResponse(recommendation: Recommendation): RecommendationResponse {
        return RecommendationResponse(
            id = recommendation.id,
            emotionId = recommendation.emotion.id,
            emotionName = recommendation.emotion.name,
            type = recommendation.type,
            content = recommendation.content,
            durationMinutes = recommendation.durationMinutes
        )
    }
}