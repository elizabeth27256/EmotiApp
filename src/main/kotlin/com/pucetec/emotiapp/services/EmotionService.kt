package com.pucetec.emotiapp.services

import com.pucetec.emotiapp.mappers.EmotionMapper
import com.pucetec.emotiapp.models.request.EmotionRequest
import com.pucetec.emotiapp.models.responses.EmotionResponse
import com.pucetec.emotiapp.repositories.EmotionRepository
import org.springframework.stereotype.Service

@Service
class EmotionService(
    private val emotionRepository: EmotionRepository,
    private val emotionMapper: EmotionMapper
) {

    fun save(request: EmotionRequest): EmotionResponse {
        if (emotionRepository.findByName(request.name) != null) {
            throw RuntimeException("Emotion already exists")
        }

        val emotion = emotionMapper.toEntity(request)
        val savedEmotion = emotionRepository.save(emotion)

        return emotionMapper.toResponse(savedEmotion)
    }

    fun findAll(): List<EmotionResponse> =
        emotionRepository.findAll().map { emotionMapper.toResponse(it) }

    fun findById(id: Long): EmotionResponse {
        val emotion = emotionRepository.findById(id)
            .orElseThrow { RuntimeException("Emotion not found") }
        return emotionMapper.toResponse(emotion)
    }

    fun update(id: Long, request: EmotionRequest): EmotionResponse {
        val emotion = emotionRepository.findById(id)
            .orElseThrow { RuntimeException("Emotion not found") }
        emotion.name = request.name
        emotion.description = request.description
        emotion.level = request.level
        val updated = emotionRepository.save(emotion)
        return emotionMapper.toResponse(updated)
    }

    fun delete(id: Long) {
        val emotion = emotionRepository.findById(id)
            .orElseThrow { RuntimeException("Emotion not found") }
        emotionRepository.delete(emotion)
    }
}