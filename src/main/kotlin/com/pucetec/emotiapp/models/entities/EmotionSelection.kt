package com.pucetec.emotiapp.models.entities

import jakarta.persistence.*
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import java.time.LocalDateTime

@Entity
@Table(name = "emotion_selections")

class EmotionSelection(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    var user: Users,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emotion_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    var emotion: Emotion,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recommendation_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    var recommendation: Recommendation,

    @Column(name = "selected_at", nullable = false)
    var selectedAt: LocalDateTime = LocalDateTime.now()

) : BaseEntity() {

    // Propiedad derivada para acceder al usename de la tabla usuarios
    val username: String
        get() = user.username
}