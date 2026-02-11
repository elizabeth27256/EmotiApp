package com.pucetec.emotiapp.repositories

import com.pucetec.emotiapp.models.entities.EmotionSelection
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface EmotionSelectionRepository : JpaRepository<EmotionSelection, Long> {

    fun findByUserId(userId: Long): List<EmotionSelection>

    fun findByUserIdAndSelectedAtAfter(
        userId: Long,
        selectedAt: LocalDateTime
    ): List<EmotionSelection>

    // 🔥 ESTE ES EL QUE TE FALTABA 🔥
    @Query("""
        select es.recommendation.id
        from EmotionSelection es
        where es.user.id = :userId
          and es.emotion.id = :emotionId
    """)
    fun findSeenRecommendationIds(
        @Param("userId") userId: Long,
        @Param("emotionId") emotionId: Long
    ): List<Long>
}
