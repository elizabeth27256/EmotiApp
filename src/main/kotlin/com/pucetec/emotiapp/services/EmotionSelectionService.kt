package com.pucetec.emotiapp.services

import com.pucetec.emotiapp.exceptions.EmotionNotFoundException
import com.pucetec.emotiapp.exceptions.EmotionSelectionNotFoundException
import com.pucetec.emotiapp.exceptions.UserNotFoundException
import com.pucetec.emotiapp.mappers.EmotionSelectionMapper
import com.pucetec.emotiapp.models.entities.RecommendationType
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
        val user = usersRepository.findById(userId).orElse(null)
            ?: throw UserNotFoundException("User with id $userId not found")

        val emotion = emotionRepository.findById(request.emotionId).orElse(null)
            ?: throw EmotionNotFoundException("Emotion with id ${request.emotionId} not found")

        // Obtener IDs de recomendaciones ya vistas por este usuario para esta emoción
        val seenRecommendationIds = emotionSelectionRepository
            .findByUserIdAndEmotionId(userId, emotion.id)
            .map { it.recommendation.id }

        // Obtener recomendación única (sin repetir)
        val recommendationResponse = recommendationService.getUniqueRandomByEmotionIdAndType(
            emotionId = emotion.id,
            type = request.type,
            excludeIds = seenRecommendationIds
        )

        // Convertir Response a Entity
        val recommendation = recommendationService.getRecommendationEntityById(recommendationResponse.id)

        val selection = emotionSelectionMapper.toEntity(user, emotion, recommendation)
        val savedSelection = emotionSelectionRepository.save(selection)

        return emotionSelectionMapper.toResponse(savedSelection)
    }

    fun findById(id: Long): EmotionSelectionResponse {
        val selection = emotionSelectionRepository.findById(id).orElse(null)
            ?: throw EmotionSelectionNotFoundException("Selection with id $id not found")
        return emotionSelectionMapper.toResponse(selection)
    }

    fun findByUserId(userId: Long, afterDate: LocalDateTime?): List<EmotionSelectionResponse> {
        usersRepository.findById(userId).orElse(null)
            ?: throw UserNotFoundException("User with id $userId not found")

        val selections = if (afterDate != null) {
            emotionSelectionRepository.findByUserIdAndSelectedAtAfter(userId, afterDate)
        } else {
            emotionSelectionRepository.findByUserId(userId)
        }

        return selections.map { emotionSelectionMapper.toResponse(it) }
    }

    fun findAll(): List<EmotionSelectionResponse> {
        return emotionSelectionRepository.findAll()
            .map { emotionSelectionMapper.toResponse(it) }
    }

    fun delete(id: Long) {
        val selection = emotionSelectionRepository.findById(id).orElse(null)
            ?: throw EmotionSelectionNotFoundException("Selection with id $id not found")
        emotionSelectionRepository.delete(selection)
    }
}