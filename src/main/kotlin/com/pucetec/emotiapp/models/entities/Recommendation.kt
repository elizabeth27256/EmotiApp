package com.pucetec.emotiapp.models.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table


@Entity
@Table(name = "recommendations")
class Recommendation(

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emotion_id", nullable = false)
    var emotion: Emotion,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    var type: RecommendationType,

    @Column(nullable = false, columnDefinition = "TEXT")
    var content: String,

    @Column(name = "duration_minutes")
    var durationMinutes: Int?

) : BaseEntity()

enum class RecommendationType {
    PHRASE,
    EXERCISE
}