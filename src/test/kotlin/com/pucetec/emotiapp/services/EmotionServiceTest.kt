package com.pucetec.emotiapp.services

import com.pucetec.emotiapp.exceptions.EmotionAlreadyExistsException
import com.pucetec.emotiapp.exceptions.EmotionNotFoundException
import com.pucetec.emotiapp.mappers.EmotionMapper
import com.pucetec.emotiapp.models.entities.Emotion
import com.pucetec.emotiapp.models.request.EmotionRequest
import com.pucetec.emotiapp.repositories.EmotionRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.*
import java.util.*

class EmotionServiceTest {

    private lateinit var emotionRepository: EmotionRepository
    private lateinit var emotionMapper: EmotionMapper
    private lateinit var emotionService: EmotionService

    @BeforeEach
    fun setup() {
        emotionRepository = mock(EmotionRepository::class.java)
        emotionMapper = EmotionMapper()
        emotionService = EmotionService(emotionRepository, emotionMapper)
    }

    @Test
    fun `save should create emotion successfully`() {
        val request = EmotionRequest(name = "Happy")
        val emotion = Emotion(name = "Happy")
        emotion.id = 1L

        `when`(emotionRepository.findByName("Happy")).thenReturn(null)
        `when`(emotionRepository.save(any(Emotion::class.java))).thenReturn(emotion)

        val response = emotionService.save(request)

        assertNotNull(response)
        assertEquals("Happy", response.name)
        verify(emotionRepository, times(1)).findByName("Happy")
        verify(emotionRepository, times(1)).save(any(Emotion::class.java))
    }

    @Test
    fun `save should throw EmotionAlreadyExistsException when emotion already exists`() {
        val request = EmotionRequest(name = "Happy")
        val existingEmotion = Emotion(name = "Happy")

        `when`(emotionRepository.findByName("Happy")).thenReturn(existingEmotion)

        val exception = assertThrows<EmotionAlreadyExistsException> {
            emotionService.save(request)
        }
        assertEquals("Emotion with name 'Happy' already exists", exception.message)
        verify(emotionRepository, never()).save(any(Emotion::class.java))
    }

    @Test
    fun `findAll should return all emotions`() {
        val emotions = listOf(
            Emotion(name = "Happy").apply { id = 1L },
            Emotion(name = "Sad").apply { id = 2L }
        )

        `when`(emotionRepository.findAll()).thenReturn(emotions)

        val response = emotionService.findAll()

        assertEquals(2, response.size)
        assertEquals("Happy", response[0].name)
        assertEquals("Sad", response[1].name)
        verify(emotionRepository, times(1)).findAll()
    }

    @Test
    fun `findById should return emotion when exists`() {
        val emotion = Emotion(name = "Happy").apply { id = 1L }

        `when`(emotionRepository.findById(1L)).thenReturn(Optional.of(emotion))

        val response = emotionService.findById(1L)

        assertNotNull(response)
        assertEquals(1L, response.id)
        assertEquals("Happy", response.name)
        verify(emotionRepository, times(1)).findById(1L)
    }

    @Test
    fun `findById should throw EmotionNotFoundException when not found`() {
        `when`(emotionRepository.findById(1L)).thenReturn(Optional.empty())

        val exception = assertThrows<EmotionNotFoundException> {
            emotionService.findById(1L)
        }
        assertEquals("Emotion not found with id: 1", exception.message)
    }

    @Test
    fun `update should update emotion successfully`() {
        val request = EmotionRequest(name = "Very Happy")
        val emotion = Emotion(name = "Happy").apply { id = 1L }
        val updatedEmotion = Emotion(name = "Very Happy").apply { id = 1L }

        `when`(emotionRepository.findById(1L)).thenReturn(Optional.of(emotion))
        `when`(emotionRepository.save(emotion)).thenReturn(updatedEmotion)

        val response = emotionService.update(1L, request)

        assertEquals("Very Happy", response.name)
        verify(emotionRepository, times(1)).save(emotion)
    }

    @Test
    fun `update should throw EmotionNotFoundException when emotion not found`() {
        val request = EmotionRequest(name = "Happy")

        `when`(emotionRepository.findById(1L)).thenReturn(Optional.empty())

        val exception = assertThrows<EmotionNotFoundException> {
            emotionService.update(1L, request)
        }
        assertEquals("Emotion not found with id: 1", exception.message)
    }

    @Test
    fun `delete should delete emotion when exists`() {
        val emotion = Emotion(name = "Happy").apply { id = 1L }

        `when`(emotionRepository.findById(1L)).thenReturn(Optional.of(emotion))

        emotionService.delete(1L)

        verify(emotionRepository, times(1)).delete(emotion)
    }

    @Test
    fun `delete should throw EmotionNotFoundException when emotion not found`() {
        `when`(emotionRepository.findById(1L)).thenReturn(Optional.empty())

        val exception = assertThrows<EmotionNotFoundException> {
            emotionService.delete(1L)
        }
        assertEquals("Emotion not found with id: 1", exception.message)
        verify(emotionRepository, never()).delete(any(Emotion::class.java))
    }
}