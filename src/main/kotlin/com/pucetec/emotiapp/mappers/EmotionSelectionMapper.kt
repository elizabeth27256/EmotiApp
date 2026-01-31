package com.pucetec.emotiapp.mappers

import com.pucetec.emotiapp.models.entities.EmotionSelection
import com.pucetec.emotiapp.models.entities.Emotion
import com.pucetec.emotiapp.models.entities.Recommendation
import com.pucetec.emotiapp.models.entities.Users
import com.pucetec.emotiapp.models.responses.EmotionSelectionResponse
import org.springframework.stereotype.Component

@Component
class EmotionSelectionMapper {

    fun toEntity(
        user: Users,
        emotion: Emotion,
        recommendation: Recommendation
    ): EmotionSelection {
        return EmotionSelection(
            user = user,
            emotion = emotion,
            recommendation = recommendation
        )
    }

    fun toResponse(selection: EmotionSelection): EmotionSelectionResponse {
        return EmotionSelectionResponse(
            id = selection.id,
            username = selection.user.username,
            emotionId = selection.emotion.id,
            emotionName = selection.emotion.name,
            recommendationId = selection.recommendation.id,
            selectedAt = selection.createdAt
        )
    }
}