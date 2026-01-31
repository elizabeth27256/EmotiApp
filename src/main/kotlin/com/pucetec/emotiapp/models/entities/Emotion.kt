package com.pucetec.emotiapp.models.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "emotions")
class Emotion(

    @Column(nullable = false)
    var name: String,

) : BaseEntity()
