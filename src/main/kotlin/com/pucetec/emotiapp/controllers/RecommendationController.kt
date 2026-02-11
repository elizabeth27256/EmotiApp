package com.pucetec.emotiapp.controllers

import com.pucetec.emotiapp.models.entities.RecommendationType
import com.pucetec.emotiapp.models.request.RecommendationRequest
import com.pucetec.emotiapp.models.responses.RecommendationResponse
import com.pucetec.emotiapp.services.RecommendationService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@CrossOrigin(origins = ["http://localhost:8100"])
@RestController
@RequestMapping("/api")
class RecommendationController(
    private val recommendationService: RecommendationService
) {

    // ADMIN: crear recomendación
    @PostMapping("/emotions/{emotionId}/recommendations")
    @ResponseStatus(HttpStatus.CREATED)
    fun save(
        @PathVariable emotionId: Long,
        @RequestBody recommendation: RecommendationRequest
    ): RecommendationResponse {
        return recommendationService.save(emotionId, recommendation)
    }

    // ADMIN: ver recomendaciones por emoción
    @GetMapping("/emotions/{emotionId}/recommendations")
    fun findByEmotionId(
        @PathVariable emotionId: Long
    ): List<RecommendationResponse> {
        return recommendationService.findByEmotionId(emotionId)
    }

    // ADMIN: eliminar
    @DeleteMapping("/recommendations/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: Long) {
        recommendationService.delete(id)
    }
}