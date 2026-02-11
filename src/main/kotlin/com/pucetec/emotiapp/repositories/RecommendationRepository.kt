package com.pucetec.emotiapp.repositories

import com.pucetec.emotiapp.models.entities.Recommendation
import com.pucetec.emotiapp.models.entities.RecommendationType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface RecommendationRepository : JpaRepository<Recommendation, Long> {

    // Todas las recomendaciones para una emoción
    fun findByEmotionId(emotionId: Long): List<Recommendation>

    // Recomendaciones por tipo
    fun findByType(type: RecommendationType): List<Recommendation>

    // Recomendaciones por emoción y tipo
    fun findByEmotionIdAndType(
        emotionId: Long,
        type: RecommendationType
    ): List<Recommendation>

}
