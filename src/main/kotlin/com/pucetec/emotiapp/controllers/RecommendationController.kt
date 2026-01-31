package com.pucetec.emotiapp.controllers

import com.pucetec.emotiapp.models.entities.RecommendationType
import com.pucetec.emotiapp.models.request.RecommendationRequest
import com.pucetec.emotiapp.models.responses.RecommendationResponse
import com.pucetec.emotiapp.services.RecommendationService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class RecommendationController(private val recommendationService: RecommendationService) {


    @PostMapping("/emotions/{emotionId}/recommendations")
    @ResponseStatus(HttpStatus.CREATED)
    fun save(
        @PathVariable emotionId: Long,
        @RequestBody recommendation: RecommendationRequest
    ): RecommendationResponse {
        return recommendationService.save(emotionId, recommendation)
    }

    @GetMapping("/recommendations/{id}")
    fun findById(@PathVariable id: Long): RecommendationResponse {
        return recommendationService.findById(id)
    }

    @GetMapping("/emotions/{emotionId}/recommendations")
    fun findByEmotionId(@PathVariable emotionId: Long): List<RecommendationResponse> {
        return recommendationService.findByEmotionId(emotionId)
    }

    @DeleteMapping("/recommendations/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: Long) {
        recommendationService.delete(id)
    }

    // Aleatorización (Core de la App Móvil)

    @GetMapping("/emotions/{emotionId}/recommendations/random")
    fun getRandomRecommendation(
        @PathVariable emotionId: Long,
        @RequestParam type: RecommendationType,
        @RequestParam(required = false) excludeIds: List<Long>?
    ): RecommendationResponse {
        return recommendationService.getUniqueRandomByEmotionIdAndType(
            emotionId,
            type,
            excludeIds ?: emptyList()
        )
    }
}