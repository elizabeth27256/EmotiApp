package com.pucetec.emotiapp.models.entities

import jakarta.persistence.*

@Entity
@Table(
    name = "recommendation_history",
    uniqueConstraints = [
        UniqueConstraint(
            columnNames = ["user_id", "recommendation_id"]
        )
    ]
)
class RecommendationHistory(

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: Users,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recommendation_id", nullable = false)
    val recommendation: Recommendation

) : BaseEntity()