package com.pucetec.emotiapp.repositories

import com.pucetec.emotiapp.models.entities.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface UserRepository : JpaRepository<User, Long> {

    fun findByUserIdentifier(userIdentifier: String): List<User>

    fun findByUserIdentifierAndSelectedAtAfter(
        userIdentifier: String,
        selectedAt: LocalDateTime
    ): List<User>
}