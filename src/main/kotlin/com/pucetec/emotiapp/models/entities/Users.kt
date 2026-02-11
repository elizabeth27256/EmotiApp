package com.pucetec.emotiapp.models.entities

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "users")
class Users(

    @Column(nullable = false, unique = true, length = 100)
    var username: String = "",

    @Column(nullable = false, unique = true)
    var email: String = "",

    @JsonIgnore
    @Column(nullable = false)
    var password: String = ""

) : BaseEntity()
