package com.pucetec.emotiapp.models.entities

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "emotion_selections")
open class EmotionSelection(

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: Users,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emotion_id", nullable = false)
    val emotion: Emotion,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recommendation_id", nullable = false)
    val recommendation: Recommendation,

    @Column(name = "selected_at", nullable = false)
    val selectedAt: LocalDateTime = LocalDateTime.now()

) : BaseEntity()