package com.pucetec.emotiapp.controllers

import com.pucetec.emotiapp.models.request.EmotionRequest
import com.pucetec.emotiapp.models.responses.EmotionResponse
import com.pucetec.emotiapp.services.EmotionService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/emotions")
class EmotionController(private val emotionService: EmotionService) {

    // Create
    @PostMapping
    fun save(@RequestBody emotion: EmotionRequest): EmotionResponse {
        return emotionService.save(emotion)
    }

    // Busca todas las emociones
    @GetMapping
    fun findAll(): List<EmotionResponse> {
        return emotionService.findAll()
    }

    // Busca por el id de la emoicion
    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): EmotionResponse {
        return emotionService.findById(id)
    }

    // UPDATE
    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody emotion: EmotionRequest): EmotionResponse {
        return emotionService.update(id, emotion)
    }

    // DELETE
    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long) {
        emotionService.delete(id)
    }
}
