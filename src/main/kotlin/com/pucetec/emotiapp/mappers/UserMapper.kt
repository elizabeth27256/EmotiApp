package com.pucetec.emotiapp.mappers

import com.pucetec.emotiapp.models.entities.Emotion
import com.pucetec.emotiapp.models.entities.Recommendation
import com.pucetec.emotiapp.models.entities.User
import com.pucetec.emotiapp.models.request.UserRequest
import com.pucetec.emotiapp.models.responses.UserResponse
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class UserMapper {

    fun toEntity(request: UserRequest, emotion: Emotion, recommendation: Recommendation?): User {
        return User(
            userIdentifier = request.userIdentifier,
            emotion = emotion,
            recommendation = recommendation,
            selectedAt = LocalDateTime.now()
        )
    }

    fun toResponse(user: User): UserResponse {
        return UserResponse(
            id = user.id,
            userIdentifier = user.userIdentifier,
            emotionId = user.emotion.id,
            emotionName = user.emotion.name,
            recommendationId = user.recommendation?.id,
            selectedAt = user.selectedAt
        )
    }
}