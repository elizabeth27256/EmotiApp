package com.pucetec.emotiapp.controllers

import com.pucetec.emotiapp.models.entities.RecommendationType
import com.pucetec.emotiapp.models.request.RecommendationRequest
import com.pucetec.emotiapp.models.responses.RecommendationResponse
import com.pucetec.emotiapp.services.RecommendationService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class RecommendationController(private val recommendationService: RecommendationService) {

    @PostMapping("/emotions/{emotionId}/recommendations")
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
    fun findByEmotionId(
        @PathVariable emotionId: Long,
        @RequestParam(required = false) type: RecommendationType?
    ): List<RecommendationResponse> {
        return if (type != null) {
            recommendationService.findByEmotionIdAndType(emotionId, type)
        } else {
            recommendationService.findByEmotionId(emotionId)
        }
    }

    @GetMapping("/recommendations")
    fun findByType(@RequestParam type: RecommendationType): List<RecommendationResponse> {
        return recommendationService.findByType(type)
    }

    @DeleteMapping("/recommendations/{id}")
    fun delete(@PathVariable id: Long) {
        recommendationService.delete(id)
    }
}