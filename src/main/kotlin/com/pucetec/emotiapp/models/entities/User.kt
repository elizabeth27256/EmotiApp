package com.pucetec.emotiapp.models.entities

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "users")
class User(

    @Column(name = "user_identifier", nullable = false, length = 100)
    var userIdentifier: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emotion_id", nullable = false)
    var emotion: Emotion,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recommendation_id")
    var recommendation: Recommendation?,

    @Column(name = "selected_at", nullable = false)
    var selectedAt: LocalDateTime

) : BaseEntity()