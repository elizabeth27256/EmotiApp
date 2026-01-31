package com.pucetec.emotiapp.services

import com.pucetec.emotiapp.exceptions.EmotionNotFoundException
import com.pucetec.emotiapp.mappers.RecommendationMapper
import com.pucetec.emotiapp.models.entities.Emotion
import com.pucetec.emotiapp.models.entities.Recommendation
import com.pucetec.emotiapp.models.entities.RecommendationType
import com.pucetec.emotiapp.models.request.RecommendationRequest
import com.pucetec.emotiapp.models.responses.RecommendationResponse
import com.pucetec.emotiapp.repositories.EmotionRepository
import com.pucetec.emotiapp.repositories.RecommendationRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.Mockito.*
import java.util.*

class RecommendationServiceTest {

    private lateinit var recommendationRepository: RecommendationRepository
    private lateinit var emotionRepository: EmotionRepository
    private lateinit var recommendationMapper: RecommendationMapper
    private lateinit var service: RecommendationService

    private lateinit var testEmotion: Emotion

    // Función de ayuda para evitar NullPointerException con any() en Kotlin
    private fun <T> anyNotNull(): T {
        any<T>()
        return null as T
    }

    @BeforeEach
    fun setup() {
        recommendationRepository = mock(RecommendationRepository::class.java)
        emotionRepository = mock(EmotionRepository::class.java)
        recommendationMapper = mock(RecommendationMapper::class.java)

        // Se pasan los TRES parámetros requeridos por el servicio
        service = RecommendationService(
            recommendationRepository,
            emotionRepository,
            recommendationMapper
        )

        testEmotion = Emotion("Happy").apply { id = 1L }
    }

    @Test
    fun `save debe crear recomendacion exitosamente`() {
        val request = RecommendationRequest(RecommendationType.PHRASE, "Keep smiling!", null)
        val recommendation = Recommendation(testEmotion, request.type, request.content, request.durationMinutes)
        val responseExpected = RecommendationResponse(10L, 1L, "Happy", RecommendationType.PHRASE, "Keep smiling!", null)

        `when`(emotionRepository.findById(1L)).thenReturn(Optional.of(testEmotion))
        `when`(recommendationRepository.save(anyNotNull())).thenReturn(recommendation)
        `when`(recommendationMapper.toResponse(anyNotNull())).thenReturn(responseExpected)

        val response = service.save(1L, request)

        assertNotNull(response)
        assertEquals("Keep smiling!", response.content)
        verify(recommendationRepository).save(anyNotNull())
    }

    @Test
    fun `save debe lanzar EmotionNotFoundException si la emocion no existe`() {
        `when`(emotionRepository.findById(anyLong())).thenReturn(Optional.empty())

        assertThrows<EmotionNotFoundException> {
            service.save(1L, RecommendationRequest(RecommendationType.PHRASE, "Content", null))
        }
    }

    @Test
    fun `getUniqueRandomByEmotionIdAndType debe filtrar las ya vistas`() {
        val rec1 = Recommendation(testEmotion, RecommendationType.PHRASE, "R1", null).apply { id = 10L }
        val rec2 = Recommendation(testEmotion, RecommendationType.PHRASE, "R2", null).apply { id = 11L }
        val res2 = RecommendationResponse(11L, 1L, "Happy", RecommendationType.PHRASE, "R2", null)

        `when`(emotionRepository.findById(1L)).thenReturn(Optional.of(testEmotion))
        `when`(recommendationRepository.findByEmotionIdAndType(1L, RecommendationType.PHRASE))
            .thenReturn(listOf(rec1, rec2))
        `when`(recommendationMapper.toResponse(anyNotNull())).thenReturn(res2)

        val result = service.getUniqueRandomByEmotionIdAndType(1L, RecommendationType.PHRASE, listOf(10L))

        assertEquals(11L, result.id)
        assertEquals("R2", result.content)
    }

    @Test
    fun `getUniqueRandomByEmotionIdAndType debe reciclar si todas fueron vistas`() {
        val rec1 = Recommendation(testEmotion, RecommendationType.PHRASE, "R1", null).apply { id = 10L }
        val res1 = RecommendationResponse(10L, 1L, "Happy", RecommendationType.PHRASE, "R1", null)

        `when`(emotionRepository.findById(1L)).thenReturn(Optional.of(testEmotion))
        `when`(recommendationRepository.findByEmotionIdAndType(1L, RecommendationType.PHRASE))
            .thenReturn(listOf(rec1))
        `when`(recommendationMapper.toResponse(anyNotNull())).thenReturn(res1)

        val result = service.getUniqueRandomByEmotionIdAndType(1L, RecommendationType.PHRASE, listOf(10L))

        assertEquals(10L, result.id)
    }

    @Test
    fun `delete debe llamar al repositorio correctamente`() {
        val rec = Recommendation(testEmotion, RecommendationType.PHRASE, "Content", null).apply { id = 1L }
        `when`(recommendationRepository.findById(1L)).thenReturn(Optional.of(rec))

        service.delete(1L)

        verify(recommendationRepository).delete(rec)
    }
}