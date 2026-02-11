package com.pucetec.emotiapp.repositories

import com.pucetec.emotiapp.models.entities.RecommendationHistory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface RecommendationHistoryRepository :
    JpaRepository<RecommendationHistory, Long> {

    @Query("""
        SELECT rh.recommendation.id
        FROM RecommendationHistory rh
        WHERE rh.user.id = :userId
    """)
    fun findSeenRecommendationIdsByUserId(
        @Param("userId") userId: Long
    ): List<Long>

    fun existsByUserIdAndRecommendationId(
        userId: Long,
        recommendationId: Long
    ): Boolean
}