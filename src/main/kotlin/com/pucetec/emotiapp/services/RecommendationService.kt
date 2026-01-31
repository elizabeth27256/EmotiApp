package com.pucetec.emotiapp.services

import com.pucetec.emotiapp.exceptions.EmotionNotFoundException
import com.pucetec.emotiapp.exceptions.InvalidRecommendationException
import com.pucetec.emotiapp.exceptions.RecommendationNotFoundException
import com.pucetec.emotiapp.mappers.RecommendationMapper
import com.pucetec.emotiapp.models.entities.Recommendation
import com.pucetec.emotiapp.models.entities.RecommendationType
import com.pucetec.emotiapp.models.request.RecommendationRequest
import com.pucetec.emotiapp.models.responses.RecommendationResponse
import com.pucetec.emotiapp.repositories.EmotionRepository
import com.pucetec.emotiapp.repositories.RecommendationRepository
import org.springframework.stereotype.Service

@Service
class RecommendationService(
    private val recommendationRepository: RecommendationRepository,
    private val emotionRepository: EmotionRepository,
    private val recommendationMapper: RecommendationMapper
) {

    fun save(emotionId: Long, request: RecommendationRequest): RecommendationResponse {
        val emotion = validateAndGetEmotion(emotionId)

        if (request.type == RecommendationType.EXERCISE && request.durationMinutes == null) {
            throw InvalidRecommendationException("Duration is required for EXERCISE recommendations")
        }

        val recommendation = recommendationMapper.toEntity(request, emotion)
        val savedRecommendation = recommendationRepository.save(recommendation)

        return recommendationMapper.toResponse(savedRecommendation)
    }

    fun findById(id: Long): RecommendationResponse {
        val recommendation = getRecommendationEntityById(id)
        return recommendationMapper.toResponse(recommendation)
    }

    fun findByEmotionId(emotionId: Long): List<RecommendationResponse> {
        validateAndGetEmotion(emotionId) // Reutilizamos para validar
        return recommendationRepository.findByEmotionId(emotionId)
            .map { recommendationMapper.toResponse(it) }
    }

    fun delete(id: Long) {
        val recommendation = getRecommendationEntityById(id)
        recommendationRepository.delete(recommendation)
    }

    // Aleatorización (Core de la App)

    fun getUniqueRandomByEmotionIdAndType(
        emotionId: Long,
        type: RecommendationType,
        excludeIds: List<Long> = emptyList()
    ): RecommendationResponse {
        validateAndGetEmotion(emotionId)

        val allRecommendations = recommendationRepository.findByEmotionIdAndType(emotionId, type)

        if (allRecommendations.isEmpty()) {
            throw RecommendationNotFoundException(
                "No recommendations of type $type found for emotion with id $emotionId"
            )
        }

        val unseenRecommendations = allRecommendations.filter { it.id !in excludeIds }

        // Si ya vio todas, reiniciamos el ciclo para que no se quede sin contenido
        val availableRecommendations = unseenRecommendations.ifEmpty { allRecommendations }

        return recommendationMapper.toResponse(availableRecommendations.random())
    }

    fun getRecommendationEntityById(id: Long): Recommendation {
        return recommendationRepository.findById(id)
            .orElseThrow { RecommendationNotFoundException("Recommendation with id $id not found") }
    }

    private fun validateAndGetEmotion(emotionId: Long) =
        emotionRepository.findById(emotionId)
            .orElseThrow { EmotionNotFoundException("Emotion with id $emotionId not found") }
}