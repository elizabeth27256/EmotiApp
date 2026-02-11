package com.pucetec.emotiapp.services

import com.pucetec.emotiapp.exceptions.EmotionNotFoundException
import com.pucetec.emotiapp.exceptions.EmotionSelectionNotFoundException
import com.pucetec.emotiapp.exceptions.UserNotFoundException
import com.pucetec.emotiapp.mappers.EmotionSelectionMapper
import com.pucetec.emotiapp.models.request.EmotionSelectionRequest
import com.pucetec.emotiapp.models.responses.EmotionSelectionResponse
import com.pucetec.emotiapp.repositories.EmotionRepository
import com.pucetec.emotiapp.repositories.EmotionSelectionRepository
import com.pucetec.emotiapp.repositories.UsersRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class EmotionSelectionService(
    private val emotionSelectionRepository: EmotionSelectionRepository,
    private val usersRepository: UsersRepository,
    private val emotionRepository: EmotionRepository,
    private val recommendationService: RecommendationService,
    private val emotionSelectionMapper: EmotionSelectionMapper
) {

    fun save(userId: Long, request: EmotionSelectionRequest): EmotionSelectionResponse {

        val user = usersRepository.findById(userId)
            .orElseThrow { UserNotFoundException("User with id $userId not found") }

        val emotion = emotionRepository.findById(request.emotionId)
            .orElseThrow { EmotionNotFoundException("Emotion with id ${request.emotionId} not found") }

        // ✅ Obtener IDs de recomendaciones ya vistas
        val seenRecommendationIds =
            emotionSelectionRepository.findSeenRecommendationIds(
                userId = userId,
                emotionId = emotion.id
            )

        // ✅ Obtener recomendación única aleatoria
        val recommendationResponse =
            recommendationService.getUniqueRandomByEmotionIdAndType(
                emotionId = emotion.id,
                type = request.recommendationType,
                excludeIds = seenRecommendationIds
            )

        val recommendation =
            recommendationService.getRecommendationEntityById(recommendationResponse.id)

        val selection =
            emotionSelectionMapper.toEntity(user, emotion, recommendation)

        val saved = emotionSelectionRepository.save(selection)

        return emotionSelectionMapper.toResponse(saved)
    }

    fun findById(id: Long): EmotionSelectionResponse =
        emotionSelectionRepository.findById(id)
            .orElseThrow {
                EmotionSelectionNotFoundException("Selection with id $id not found")
            }
            .let { emotionSelectionMapper.toResponse(it) }

    fun findByUserId(
        userId: Long,
        afterDate: LocalDateTime?
    ): List<EmotionSelectionResponse> {

        usersRepository.findById(userId)
            .orElseThrow { UserNotFoundException("User with id $userId not found") }

        // 🔑 Ahora obtenemos las entidades y las mapeamos
        val selections = if (afterDate == null) {
            emotionSelectionRepository.findByUserId(userId)
        } else {
            emotionSelectionRepository.findByUserIdAndSelectedAtAfter(userId, afterDate)
        }

        return selections.map { emotionSelectionMapper.toResponse(it) }
    }

    fun findAll(): List<EmotionSelectionResponse> =
        emotionSelectionRepository.findAll()
            .map { emotionSelectionMapper.toResponse(it) }

    fun delete(id: Long) {
        val selection = emotionSelectionRepository.findById(id)
            .orElseThrow {
                EmotionSelectionNotFoundException("Selection with id $id not found")
            }

        emotionSelectionRepository.delete(selection)
    }
}