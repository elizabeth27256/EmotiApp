package com.pucetec.emotiapp.repositories

import com.pucetec.emotiapp.models.entities.EmotionSelection
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface EmotionSelectionRepository : JpaRepository<EmotionSelection, Long> {

    fun findByUserId(userId: Long): List<EmotionSelection>

    fun findByUserIdAndEmotionId(userId: Long, emotionId: Long): List<EmotionSelection>

    fun findByUserIdAndSelectedAtAfter(userId: Long, afterDate: LocalDateTime): List<EmotionSelection>
}