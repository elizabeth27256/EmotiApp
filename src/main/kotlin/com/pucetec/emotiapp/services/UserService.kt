package com.pucetec.emotiapp.services

import com.pucetec.emotiapp.mappers.UserMapper
import com.pucetec.emotiapp.models.request.UserRequest
import com.pucetec.emotiapp.models.responses.UserResponse
import com.pucetec.emotiapp.repositories.EmotionRepository
import com.pucetec.emotiapp.repositories.RecommendationRepository
import com.pucetec.emotiapp.repositories.UserRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class UserService(
    private val userRepository: UserRepository,
    private val emotionRepository: EmotionRepository,
    private val recommendationRepository: RecommendationRepository,
    private val userMapper: UserMapper
) {

    fun save(request: UserRequest): UserResponse {
        if (request.userIdentifier.isBlank()) {
            throw RuntimeException("User identifier cannot be blank")
        }

        val emotion = emotionRepository.findById(request.emotionId)
            .orElseThrow { RuntimeException("Emotion not found") }

        val recommendation = request.recommendationId?.let {
            recommendationRepository.findById(it)
                .orElseThrow { RuntimeException("Recommendation not found") }
        }

        val user = userMapper.toEntity(request, emotion, recommendation)
        val savedUser = userRepository.save(user)

        return userMapper.toResponse(savedUser)
    }

    fun findById(id: Long): UserResponse {
        val user = userRepository.findById(id)
            .orElseThrow { RuntimeException("User not found") }
        return userMapper.toResponse(user)
    }

    fun findByUserIdentifier(userIdentifier: String, afterDate: LocalDateTime?): List<UserResponse> {
        if (userIdentifier.isBlank()) {
            throw RuntimeException("User identifier cannot be blank")
        }

        val users = if (afterDate != null) {
            userRepository.findByUserIdentifierAndSelectedAtAfter(userIdentifier, afterDate)
        } else {
            userRepository.findByUserIdentifier(userIdentifier)
        }

        return users.map { userMapper.toResponse(it) }
    }

    fun delete(id: Long) {
        val user = userRepository.findById(id)
            .orElseThrow { RuntimeException("User not found") }
        userRepository.delete(user)
    }
}