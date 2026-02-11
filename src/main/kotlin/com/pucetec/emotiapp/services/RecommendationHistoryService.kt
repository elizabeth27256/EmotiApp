package com.pucetec.emotiapp.services

import com.pucetec.emotiapp.models.entities.Recommendation
import com.pucetec.emotiapp.models.entities.RecommendationHistory
import com.pucetec.emotiapp.models.entities.Users
import com.pucetec.emotiapp.repositories.RecommendationHistoryRepository
import org.springframework.stereotype.Service

@Service
class RecommendationHistoryService(
    private val recommendationHistoryRepository: RecommendationHistoryRepository
) {

    fun getSeenRecommendationIds(userId: Long): List<Long> =
        recommendationHistoryRepository.findSeenRecommendationIdsByUserId(userId)

    fun saveIfNotExists(
        user: Users,
        recommendation: Recommendation
    ) {
        val exists = recommendationHistoryRepository
            .existsByUserIdAndRecommendationId(user.id, recommendation.id)

        if (!exists) {
            recommendationHistoryRepository.save(
                RecommendationHistory(user, recommendation)
            )
        }
    }
}