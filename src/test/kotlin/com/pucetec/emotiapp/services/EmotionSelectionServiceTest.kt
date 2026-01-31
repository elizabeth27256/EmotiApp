package com.pucetec.emotiapp.services

import com.pucetec.emotiapp.exceptions.EmotionNotFoundException
import com.pucetec.emotiapp.exceptions.UserNotFoundException
import com.pucetec.emotiapp.mappers.EmotionSelectionMapper
import com.pucetec.emotiapp.models.entities.*
import com.pucetec.emotiapp.models.request.EmotionSelectionRequest
import com.pucetec.emotiapp.models.responses.RecommendationResponse
import com.pucetec.emotiapp.models.responses.EmotionSelectionResponse
import com.pucetec.emotiapp.repositories.EmotionRepository
import com.pucetec.emotiapp.repositories.EmotionSelectionRepository
import com.pucetec.emotiapp.repositories.UsersRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.*
import java.util.*
import java.time.LocalDateTime

class EmotionSelectionServiceTest {

    private lateinit var emotionSelectionRepository: EmotionSelectionRepository
    private lateinit var usersRepository: UsersRepository
    private lateinit var emotionRepository: EmotionRepository
    private lateinit var recommendationService: RecommendationService
    private lateinit var mapper: EmotionSelectionMapper
    private lateinit var service: EmotionSelectionService

    private lateinit var testUser: Users
    private lateinit var testEmotion: Emotion
    private lateinit var testRecommendation: Recommendation

    // Función de ayuda para evitar el NullPointerException con any() en Kotlin
    private fun <T> anyNotNull(): T {
        any<T>()
        return null as T
    }

    @BeforeEach
    fun setup() {
        emotionSelectionRepository = mock(EmotionSelectionRepository::class.java)
        usersRepository = mock(UsersRepository::class.java)
        emotionRepository = mock(EmotionRepository::class.java)
        recommendationService = mock(RecommendationService::class.java)
        mapper = mock(EmotionSelectionMapper::class.java)

        service = EmotionSelectionService(
            emotionSelectionRepository,
            usersRepository,
            emotionRepository,
            recommendationService,
            mapper
        )

        testUser = Users("pablo", "pablo@test.com", "123").apply { id = 1L }
        testEmotion = Emotion("Happy").apply { id = 1L }
        testRecommendation = Recommendation(testEmotion, RecommendationType.PHRASE, "Keep smiling!", null).apply { id = 10L }
    }

    @Test
    fun `save debe guardar seleccion con recomendacion aleatoria correctamente`() {
        val request = EmotionSelectionRequest(1L, RecommendationType.PHRASE)
        val recResponse = RecommendationResponse(10L, 1L, "Happy", RecommendationType.PHRASE, "Keep smiling!", null)
        val savedEntity = EmotionSelection(testUser, testEmotion, testRecommendation).apply { id = 5L }

        `when`(usersRepository.findById(1L)).thenReturn(Optional.of(testUser))
        `when`(emotionRepository.findById(1L)).thenReturn(Optional.of(testEmotion))
        `when`(emotionSelectionRepository.findByUserIdAndEmotionId(1L, 1L)).thenReturn(emptyList())

        `when`(recommendationService.getUniqueRandomByEmotionIdAndType(
            eq(1L),
            anyNotNull(),
            anyNotNull()
        )).thenReturn(recResponse)

        `when`(recommendationService.getRecommendationEntityById(10L)).thenReturn(testRecommendation)

        `when`(mapper.toEntity(anyNotNull(), anyNotNull(), anyNotNull())).thenReturn(savedEntity)
        `when`(emotionSelectionRepository.save(anyNotNull())).thenReturn(savedEntity)

        // CORRECCIÓN AQUÍ: Se pasan los 6 parámetros en el orden y tipo correctos
        val finalResponse = EmotionSelectionResponse(
            id = 5L,
            username = "pablo",
            emotionId = 1L,
            emotionName = "Happy",
            recommendationId = 10L,
            selectedAt = LocalDateTime.now()
        )

        `when`(mapper.toResponse(anyNotNull())).thenReturn(finalResponse)

        val response = service.save(1L, request)

        assertNotNull(response)
        assertEquals("pablo", response.username)
        assertEquals("Happy", response.emotionName)
        verify(emotionSelectionRepository).save(any())
    }

    @Test
    fun `save debe lanzar UserNotFoundException si el usuario no existe`() {
        `when`(usersRepository.findById(anyLong())).thenReturn(Optional.empty())

        assertThrows<UserNotFoundException> {
            service.save(1L, EmotionSelectionRequest(1L, RecommendationType.PHRASE))
        }
    }

    @Test
    fun `save debe lanzar EmotionNotFoundException si la emocion no existe`() {
        `when`(usersRepository.findById(1L)).thenReturn(Optional.of(testUser))
        `when`(emotionRepository.findById(anyLong())).thenReturn(Optional.empty())

        assertThrows<EmotionNotFoundException> {
            service.save(1L, EmotionSelectionRequest(1L, RecommendationType.PHRASE))
        }
    }

    @Test
    fun `delete debe eliminar exitosamente cuando el registro existe`() {
        val selection = EmotionSelection(testUser, testEmotion, testRecommendation).apply { id = 1L }
        `when`(emotionSelectionRepository.findById(1L)).thenReturn(Optional.of(selection))

        service.delete(1L)

        verify(emotionSelectionRepository, times(1)).delete(selection)
    }
}