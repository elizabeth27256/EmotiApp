package com.pucetec.emotiapp.repositories

import com.pucetec.emotiapp.models.entities.Recommendation
import com.pucetec.emotiapp.models.entities.RecommendationType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RecommendationRepository : JpaRepository<Recommendation, Long> {

    fun findByEmotionId(emotionId: Long): List<Recommendation>

    fun findByType(type: RecommendationType): List<Recommendation>

    fun findByEmotionIdAndType(emotionId: Long, type: RecommendationType): List<Recommendation>
}