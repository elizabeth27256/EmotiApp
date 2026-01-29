package com.pucetec.emotiapp.services

import com.pucetec.emotiapp.mappers.RecommendationMapper
import com.pucetec.emotiapp.models.entities.Emotion
import com.pucetec.emotiapp.models.entities.Recommendation
import com.pucetec.emotiapp.models.entities.RecommendationType
import com.pucetec.emotiapp.models.request.RecommendationRequest
import com.pucetec.emotiapp.repositories.EmotionRepository
import com.pucetec.emotiapp.repositories.RecommendationRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.*
import java.util.*

class RecommendationServiceTest {

    private lateinit var recommendationRepository: RecommendationRepository
    private lateinit var emotionRepository: EmotionRepository
    private lateinit var recommendationMapper: RecommendationMapper
    private lateinit var recommendationService: RecommendationService

    private lateinit var testEmotion: Emotion

    @BeforeEach
    fun setup() {
        recommendationRepository = mock(RecommendationRepository::class.java)
        emotionRepository = mock(EmotionRepository::class.java)
        recommendationMapper = RecommendationMapper()
        recommendationService = RecommendationService(
            recommendationRepository,
            emotionRepository,
            recommendationMapper
        )

        testEmotion = Emotion(name = "Happy", description = "Good", level = 5).apply { id = 1L }
    }

    @Test
    fun `save should create recommendation successfully`() {
        val request = RecommendationRequest(
            type = RecommendationType.PHRASE,
            content = "Stay positive!",
            durationMinutes = null
        )
        val recommendation = Recommendation(
            emotion = testEmotion,
            type = RecommendationType.PHRASE,
            content = "Stay positive!",
            durationMinutes = null
        ).apply { id = 1L }

        `when`(emotionRepository.findById(1L)).thenReturn(Optional.of(testEmotion))
        `when`(recommendationRepository.save(any(Recommendation::class.java))).thenReturn(recommendation)

        val response = recommendationService.save(1L, request)

        assertNotNull(response)
        assertEquals("Stay positive!", response.content)
        assertEquals(RecommendationType.PHRASE, response.type)
        verify(recommendationRepository, times(1)).save(any(Recommendation::class.java))
    }

    @Test
    fun `save should throw exception when emotion not found`() {
        val request = RecommendationRequest(
            type = RecommendationType.PHRASE,
            content = "Test",
            durationMinutes = null
        )

        `when`(emotionRepository.findById(1L)).thenReturn(Optional.empty())

        val exception = assertThrows<RuntimeException> {
            recommendationService.save(1L, request)
        }
        assertEquals("Emotion not found", exception.message)
        verify(recommendationRepository, never()).save(any(Recommendation::class.java))
    }

    @Test
    fun `save should throw exception when EXERCISE without duration`() {
        val request = RecommendationRequest(
            type = RecommendationType.EXERCISE,
            content = "Breathe deeply",
            durationMinutes = null
        )

        `when`(emotionRepository.findById(1L)).thenReturn(Optional.of(testEmotion))

        val exception = assertThrows<RuntimeException> {
            recommendationService.save(1L, request)
        }
        assertEquals("Duration is required for EXERCISE recommendations", exception.message)
        verify(recommendationRepository, never()).save(any(Recommendation::class.java))
    }

    @Test
    fun `save should create EXERCISE with duration successfully`() {
        val request = RecommendationRequest(
            type = RecommendationType.EXERCISE,
            content = "Breathe deeply",
            durationMinutes = 5
        )
        val recommendation = Recommendation(
            emotion = testEmotion,
            type = RecommendationType.EXERCISE,
            content = "Breathe deeply",
            durationMinutes = 5
        ).apply { id = 1L }

        `when`(emotionRepository.findById(1L)).thenReturn(Optional.of(testEmotion))
        `when`(recommendationRepository.save(any(Recommendation::class.java))).thenReturn(recommendation)

        val response = recommendationService.save(1L, request)

        assertNotNull(response)
        assertEquals(5, response.durationMinutes)
        assertEquals(RecommendationType.EXERCISE, response.type)
    }

    @Test
    fun `findById should return recommendation when exists`() {
        val recommendation = Recommendation(
            emotion = testEmotion,
            type = RecommendationType.PHRASE,
            content = "Stay positive!",
            durationMinutes = null
        ).apply { id = 1L }

        `when`(recommendationRepository.findById(1L)).thenReturn(Optional.of(recommendation))

        val response = recommendationService.findById(1L)

        assertNotNull(response)
        assertEquals(1L, response.id)
        assertEquals("Stay positive!", response.content)
    }

    @Test
    fun `findById should throw exception when not found`() {
        `when`(recommendationRepository.findById(1L)).thenReturn(Optional.empty())

        val exception = assertThrows<RuntimeException> {
            recommendationService.findById(1L)
        }
        assertEquals("Recommendation not found", exception.message)
    }

    @Test
    fun `findByEmotionId should return recommendations`() {
        val recommendations = listOf(
            Recommendation(
                emotion = testEmotion,
                type = RecommendationType.PHRASE,
                content = "Stay positive!",
                durationMinutes = null
            ).apply { id = 1L }
        )

        `when`(emotionRepository.findById(1L)).thenReturn(Optional.of(testEmotion))
        `when`(recommendationRepository.findByEmotionId(1L)).thenReturn(recommendations)

        val response = recommendationService.findByEmotionId(1L)

        assertEquals(1, response.size)
        assertEquals("Stay positive!", response[0].content)
    }

    @Test
    fun `findByEmotionId should throw exception when emotion not found`() {
        `when`(emotionRepository.findById(1L)).thenReturn(Optional.empty())

        val exception = assertThrows<RuntimeException> {
            recommendationService.findByEmotionId(1L)
        }
        assertEquals("Emotion not found", exception.message)
    }

    @Test
    fun `findByType should return filtered recommendations`() {
        val recommendations = listOf(
            Recommendation(
                emotion = testEmotion,
                type = RecommendationType.EXERCISE,
                content = "Breathe",
                durationMinutes = 5
            ).apply { id = 1L }
        )

        `when`(recommendationRepository.findByType(RecommendationType.EXERCISE)).thenReturn(recommendations)

        val response = recommendationService.findByType(RecommendationType.EXERCISE)

        assertEquals(1, response.size)
        assertEquals(RecommendationType.EXERCISE, response[0].type)
    }

    @Test
    fun `findByEmotionIdAndType should return filtered recommendations`() {
        val recommendations = listOf(
            Recommendation(
                emotion = testEmotion,
                type = RecommendationType.PHRASE,
                content = "Stay positive!",
                durationMinutes = null
            ).apply { id = 1L }
        )

        `when`(emotionRepository.findById(1L)).thenReturn(Optional.of(testEmotion))
        `when`(recommendationRepository.findByEmotionIdAndType(1L, RecommendationType.PHRASE))
            .thenReturn(recommendations)

        val response = recommendationService.findByEmotionIdAndType(1L, RecommendationType.PHRASE)

        assertEquals(1, response.size)
        assertEquals(RecommendationType.PHRASE, response[0].type)
    }

    @Test
    fun `findByEmotionIdAndType should throw exception when emotion not found`() {
        `when`(emotionRepository.findById(1L)).thenReturn(Optional.empty())

        val exception = assertThrows<RuntimeException> {
            recommendationService.findByEmotionIdAndType(1L, RecommendationType.PHRASE)
        }
        assertEquals("Emotion not found", exception.message)
    }

    @Test
    fun `delete should delete recommendation when exists`() {
        val recommendation = Recommendation(
            emotion = testEmotion,
            type = RecommendationType.PHRASE,
            content = "Test",
            durationMinutes = null
        ).apply { id = 1L }

        `when`(recommendationRepository.findById(1L)).thenReturn(Optional.of(recommendation))

        recommendationService.delete(1L)

        verify(recommendationRepository, times(1)).delete(recommendation)
    }

    @Test
    fun `delete should throw exception when not found`() {
        `when`(recommendationRepository.findById(1L)).thenReturn(Optional.empty())

        val exception = assertThrows<RuntimeException> {
            recommendationService.delete(1L)
        }
        assertEquals("Recommendation not found", exception.message)
        verify(recommendationRepository, never()).delete(any(Recommendation::class.java))
    }
}