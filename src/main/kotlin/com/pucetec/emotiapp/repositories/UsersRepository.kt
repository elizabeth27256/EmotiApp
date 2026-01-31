package com.pucetec.emotiapp.repositories

import com.pucetec.emotiapp.models.entities.Users
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UsersRepository : JpaRepository<Users, Long> {

    fun findByUsername(username: String): Users?

    fun findByEmail(email: String): Users?

    fun existsByUsername(username: String): Boolean

    fun existsByEmail(email: String): Boolean
}