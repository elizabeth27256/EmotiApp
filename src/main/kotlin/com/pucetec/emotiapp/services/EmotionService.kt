package com.pucetec.emotiapp.services

import com.pucetec.emotiapp.models.entities.Emotion
import com.pucetec.emotiapp.models.requests.EmotionRequest
import com.pucetec.emotiapp.models.responses.EmotionResponse
import com.pucetec.emotiapp.repositories.EmotionRepository
import org.springframework.stereotype.Service

@Service
class EmotionService(private val emotionRepository: EmotionRepository) {

    // crea
    fun save(request: EmotionRequest): EmotionResponse {
        if (emotionRepository.findByName(request.name) != null) {
            throw RuntimeException("Emotion already exists")
        }

        val emotion = Emotion(
            name = request.name,
            description = request.description,
            level = request.level
        )

        val savedEmotion = emotionRepository.save(emotion)

        return EmotionResponse(
            id = savedEmotion.id,
            name = savedEmotion.name,
            description = savedEmotion.description,
            level = savedEmotion.level
        )
    }

    // lee todoID
    fun findAll(): List<EmotionResponse> =
        emotionRepository.findAll().map { e ->
            EmotionResponse(e.id, e.name, e.description, e.level)
        }

    // leer por el ID
    fun findById(id: Long): EmotionResponse {
        val e = emotionRepository.findById(id).orElseThrow { RuntimeException("Emotion not found") }
        return EmotionResponse(e.id, e.name, e.description, e.level)
    }

    // actualiza
    fun update(id: Long, request: EmotionRequest): EmotionResponse {
        val e = emotionRepository.findById(id).orElseThrow { RuntimeException("Emotion not found") }
        e.name = request.name
        e.description = request.description
        e.level = request.level
        val updated = emotionRepository.save(e)
        return EmotionResponse(updated.id, updated.name, updated.description, updated.level)
    }

    // elimina
    fun delete(id: Long) {
        val e = emotionRepository.findById(id).orElseThrow { RuntimeException("Emotion not found") }
        emotionRepository.delete(e)
    }
}
