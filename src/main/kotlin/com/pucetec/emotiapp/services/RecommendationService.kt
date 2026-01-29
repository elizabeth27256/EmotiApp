package com.pucetec.emotiapp.services

import com.pucetec.emotiapp.mappers.RecommendationMapper
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
        val emotion = emotionRepository.findById(emotionId)
            .orElseThrow { RuntimeException("Emotion not found") }

        if (request.type == RecommendationType.EXERCISE && request.durationMinutes == null) {
            throw RuntimeException("Duration is required for EXERCISE recommendations")
        }

        val recommendation = recommendationMapper.toEntity(request, emotion)
        val savedRecommendation = recommendationRepository.save(recommendation)

        return recommendationMapper.toResponse(savedRecommendation)
    }

    fun findById(id: Long): RecommendationResponse {
        val recommendation = recommendationRepository.findById(id)
            .orElseThrow { RuntimeException("Recommendation not found") }
        return recommendationMapper.toResponse(recommendation)
    }

    fun findByEmotionId(emotionId: Long): List<RecommendationResponse> {
        emotionRepository.findById(emotionId)
            .orElseThrow { RuntimeException("Emotion not found") }
        return recommendationRepository.findByEmotionId(emotionId)
            .map { recommendationMapper.toResponse(it) }
    }

    fun findByType(type: RecommendationType): List<RecommendationResponse> {
        return recommendationRepository.findByType(type)
            .map { recommendationMapper.toResponse(it) }
    }

    fun findByEmotionIdAndType(emotionId: Long, type: RecommendationType): List<RecommendationResponse> {
        emotionRepository.findById(emotionId)
            .orElseThrow { RuntimeException("Emotion not found") }
        return recommendationRepository.findByEmotionIdAndType(emotionId, type)
            .map { recommendationMapper.toResponse(it) }
    }

    fun delete(id: Long) {
        val recommendation = recommendationRepository.findById(id)
            .orElseThrow { RuntimeException("Recommendation not found") }
        recommendationRepository.delete(recommendation)
    }
}