package com.pucetec.emotiapp.services

import com.pucetec.emotiapp.mappers.UserMapper
import com.pucetec.emotiapp.models.entities.Emotion
import com.pucetec.emotiapp.models.entities.Recommendation
import com.pucetec.emotiapp.models.entities.RecommendationType
import com.pucetec.emotiapp.models.entities.User
import com.pucetec.emotiapp.models.request.UserRequest
import com.pucetec.emotiapp.repositories.EmotionRepository
import com.pucetec.emotiapp.repositories.RecommendationRepository
import com.pucetec.emotiapp.repositories.UserRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.*
import java.time.LocalDateTime
import java.util.*

class UserServiceTest {

    private lateinit var userRepository: UserRepository
    private lateinit var emotionRepository: EmotionRepository
    private lateinit var recommendationRepository: RecommendationRepository
    private lateinit var userMapper: UserMapper
    private lateinit var userService: UserService

    private lateinit var testEmotion: Emotion
    private lateinit var testRecommendation: Recommendation

    @BeforeEach
    fun setup() {
        userRepository = mock(UserRepository::class.java)
        emotionRepository = mock(EmotionRepository::class.java)
        recommendationRepository = mock(RecommendationRepository::class.java)
        userMapper = UserMapper()
        userService = UserService(
            userRepository,
            emotionRepository,
            recommendationRepository,
            userMapper
        )

        testEmotion = Emotion(name = "Happy", description = "Good", level = 5).apply { id = 1L }
        testRecommendation = Recommendation(
            emotion = testEmotion,
            type = RecommendationType.PHRASE,
            content = "Stay positive!",
            durationMinutes = null
        ).apply { id = 1L }
    }

    @Test
    fun `save should create user successfully without recommendation`() {
        val request = UserRequest(
            userIdentifier = "user123",
            emotionId = 1L,
            recommendationId = null
        )
        val user = User(
            userIdentifier = "user123",
            emotion = testEmotion,
            recommendation = null,
            selectedAt = LocalDateTime.now()
        ).apply { id = 1L }

        `when`(emotionRepository.findById(1L)).thenReturn(Optional.of(testEmotion))
        `when`(userRepository.save(any(User::class.java))).thenReturn(user)

        val response = userService.save(request)

        assertNotNull(response)
        assertEquals("user123", response.userIdentifier)
        assertEquals(1L, response.emotionId)
        assertNull(response.recommendationId)
        verify(userRepository, times(1)).save(any(User::class.java))
    }

    @Test
    fun `save should create user successfully with recommendation`() {
        val request = UserRequest(
            userIdentifier = "user123",
            emotionId = 1L,
            recommendationId = 1L
        )
        val user = User(
            userIdentifier = "user123",
            emotion = testEmotion,
            recommendation = testRecommendation,
            selectedAt = LocalDateTime.now()
        ).apply { id = 1L }

        `when`(emotionRepository.findById(1L)).thenReturn(Optional.of(testEmotion))
        `when`(recommendationRepository.findById(1L)).thenReturn(Optional.of(testRecommendation))
        `when`(userRepository.save(any(User::class.java))).thenReturn(user)

        val response = userService.save(request)

        assertNotNull(response)
        assertEquals(1L, response.recommendationId)
        verify(recommendationRepository, times(1)).findById(1L)
    }

    @Test
    fun `save should throw exception when userIdentifier is blank`() {
        val request = UserRequest(
            userIdentifier = "",
            emotionId = 1L,
            recommendationId = null
        )

        val exception = assertThrows<RuntimeException> {
            userService.save(request)
        }
        assertEquals("User identifier cannot be blank", exception.message)
        verify(userRepository, never()).save(any(User::class.java))
    }

    @Test
    fun `save should throw exception when userIdentifier has only spaces`() {
        val request = UserRequest(
            userIdentifier = "   ",
            emotionId = 1L,
            recommendationId = null
        )

        val exception = assertThrows<RuntimeException> {
            userService.save(request)
        }
        assertEquals("User identifier cannot be blank", exception.message)
    }

    @Test
    fun `save should throw exception when emotion not found`() {
        val request = UserRequest(
            userIdentifier = "user123",
            emotionId = 1L,
            recommendationId = null
        )

        `when`(emotionRepository.findById(1L)).thenReturn(Optional.empty())

        val exception = assertThrows<RuntimeException> {
            userService.save(request)
        }
        assertEquals("Emotion not found", exception.message)
        verify(userRepository, never()).save(any(User::class.java))
    }

    @Test
    fun `save should throw exception when recommendation not found`() {
        val request = UserRequest(
            userIdentifier = "user123",
            emotionId = 1L,
            recommendationId = 999L
        )

        `when`(emotionRepository.findById(1L)).thenReturn(Optional.of(testEmotion))
        `when`(recommendationRepository.findById(999L)).thenReturn(Optional.empty())

        val exception = assertThrows<RuntimeException> {
            userService.save(request)
        }
        assertEquals("Recommendation not found", exception.message)
        verify(userRepository, never()).save(any(User::class.java))
    }

    @Test
    fun `findById should return user when exists`() {
        val user = User(
            userIdentifier = "user123",
            emotion = testEmotion,
            recommendation = null,
            selectedAt = LocalDateTime.now()
        ).apply { id = 1L }

        `when`(userRepository.findById(1L)).thenReturn(Optional.of(user))

        val response = userService.findById(1L)

        assertNotNull(response)
        assertEquals(1L, response.id)
        assertEquals("user123", response.userIdentifier)
    }

    @Test
    fun `findById should throw exception when not found`() {
        `when`(userRepository.findById(1L)).thenReturn(Optional.empty())

        val exception = assertThrows<RuntimeException> {
            userService.findById(1L)
        }
        assertEquals("User not found", exception.message)
    }

    @Test
    fun `findByUserIdentifier should return users without date filter`() {
        val users = listOf(
            User(
                userIdentifier = "user123",
                emotion = testEmotion,
                recommendation = null,
                selectedAt = LocalDateTime.now()
            ).apply { id = 1L }
        )

        `when`(userRepository.findByUserIdentifier("user123")).thenReturn(users)

        val response = userService.findByUserIdentifier("user123", null)

        assertEquals(1, response.size)
        assertEquals("user123", response[0].userIdentifier)
        verify(userRepository, times(1)).findByUserIdentifier("user123")
    }

    @Test
    fun `findByUserIdentifier should return users with date filter`() {
        val afterDate = LocalDateTime.now().minusDays(7)
        val users = listOf(
            User(
                userIdentifier = "user123",
                emotion = testEmotion,
                recommendation = null,
                selectedAt = LocalDateTime.now()
            ).apply { id = 1L }
        )

        `when`(userRepository.findByUserIdentifierAndSelectedAtAfter("user123", afterDate))
            .thenReturn(users)

        val response = userService.findByUserIdentifier("user123", afterDate)

        assertEquals(1, response.size)
        verify(userRepository, times(1)).findByUserIdentifierAndSelectedAtAfter("user123", afterDate)
    }

    @Test
    fun `findByUserIdentifier should throw exception when identifier is blank`() {
        val exception = assertThrows<RuntimeException> {
            userService.findByUserIdentifier("", null)
        }
        assertEquals("User identifier cannot be blank", exception.message)
        verify(userRepository, never()).findByUserIdentifier(anyString())
    }

    @Test
    fun `findByUserIdentifier should throw exception when identifier has only spaces`() {
        val exception = assertThrows<RuntimeException> {
            userService.findByUserIdentifier("   ", null)
        }
        assertEquals("User identifier cannot be blank", exception.message)
    }

    @Test
    fun `delete should delete user when exists`() {
        val user = User(
            userIdentifier = "user123",
            emotion = testEmotion,
            recommendation = null,
            selectedAt = LocalDateTime.now()
        ).apply { id = 1L }

        `when`(userRepository.findById(1L)).thenReturn(Optional.of(user))

        userService.delete(1L)

        verify(userRepository, times(1)).delete(user)
    }

    @Test
    fun `delete should throw exception when user not found`() {
        `when`(userRepository.findById(1L)).thenReturn(Optional.empty())

        val exception = assertThrows<RuntimeException> {
            userService.delete(1L)
        }
        assertEquals("User not found", exception.message)
        verify(userRepository, never()).delete(any(User::class.java))
    }
}