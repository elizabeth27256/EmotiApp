package com.pucetec.emotiapp.repositories

import com.pucetec.emotiapp.models.entities.Emotion
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface EmotionRepository : JpaRepository<Emotion, Long> {

    fun findByName(name: String): Emotion?
}
