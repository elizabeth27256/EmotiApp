package com.pucetec.emotiapp.mappers

import com.pucetec.emotiapp.models.entities.Emotion
import com.pucetec.emotiapp.models.entities.EmotionSelection
import com.pucetec.emotiapp.models.entities.Recommendation
import com.pucetec.emotiapp.models.entities.Users
import com.pucetec.emotiapp.models.responses.EmotionSelectionResponse
import com.pucetec.emotiapp.models.responses.RecommendationData
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class EmotionSelectionMapper {

    /**
     * Request → Entity
     */
    fun toEntity(
        user: Users,
        emotion: Emotion,
        recommendation: Recommendation
    ): EmotionSelection {
        return EmotionSelection(
            user = user,
            emotion = emotion,
            recommendation = recommendation,
            selectedAt = LocalDateTime.now()
        )
    }

    /**
     * Entity → Response (con datos de recomendación completos)
     */
    fun toResponse(selection: EmotionSelection): EmotionSelectionResponse {
        return EmotionSelectionResponse(
            id = selection.id,
            username = selection.user.username,
            emotionId = selection.emotion.id,
            emotionName = selection.emotion.name,
            recommendationId = selection.recommendation.id,
            selectedAt = selection.selectedAt,
            recommendation = RecommendationData(
                id = selection.recommendation.id,
                type = selection.recommendation.type.toString(),
                content = selection.recommendation.content,
                durationMinutes = selection.recommendation.durationMinutes
            )
        )
    }
}