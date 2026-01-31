package com.pucetec.emotiapp.services

import com.pucetec.emotiapp.mappers.UsersMapper
import com.pucetec.emotiapp.models.entities.Users
import com.pucetec.emotiapp.models.request.RegisterRequest
import com.pucetec.emotiapp.models.responses.UsersResponse
import com.pucetec.emotiapp.repositories.UsersRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.*
import java.util.*

class UsersServiceTest {

    private lateinit var usersRepository: UsersRepository
    private lateinit var usersMapper: UsersMapper
    private lateinit var service: UsersService

    private fun <T> anyNotNull(): T {
        org.mockito.ArgumentMatchers.any<T>()
        return null as T
    }

    @BeforeEach
    fun setup() {
        usersRepository = mock(UsersRepository::class.java)
        usersMapper = mock(UsersMapper::class.java)
        service = UsersService(usersRepository, usersMapper)
    }

    @Test
    fun `register debe registrar exitosamente`() {
        val request = RegisterRequest("pablo", "pablo@test.com", "123")
        val userSaved = Users("pablo", "pablo@test.com", "123").apply { id = 1L }
        val responseExpected = UsersResponse(1L, "pablo", "pablo@test.com")

        `when`(usersRepository.existsByUsername(anyString())).thenReturn(false)
        `when`(usersRepository.save(anyNotNull())).thenReturn(userSaved)
        `when`(usersMapper.toResponse(anyNotNull())).thenReturn(responseExpected)

        val response = service.register(request)

        assertNotNull(response)
        assertEquals("pablo", response.username)
    }

    @Test
    fun `update debe actualizar exitosamente`() {
        val userId = 1L
        val request = RegisterRequest("pablo_edit", "pablo_edit@test.com", "new123")
        val userExisting = Users("pablo", "pablo@test.com", "123").apply { id = userId }
        val userUpdated = Users("pablo_edit", "pablo_edit@test.com", "new123").apply { id = userId }
        val responseExpected = UsersResponse(userId, "pablo_edit", "pablo_edit@test.com")

        `when`(usersRepository.findById(userId)).thenReturn(Optional.of(userExisting))
        `when`(usersRepository.save(anyNotNull())).thenReturn(userUpdated)
        `when`(usersMapper.toResponse(anyNotNull())).thenReturn(responseExpected)

        val response = service.update(userId, request)

        assertNotNull(response)
        assertEquals("pablo_edit", response.username)
    }

    @Test
    fun `register debe lanzar excepcion si username ya existe`() {
        val request = RegisterRequest("pablo", "pablo@test.com", "123")
        `when`(usersRepository.existsByUsername("pablo")).thenReturn(true)

        assertThrows<RuntimeException> {
            service.register(request)
        }
    }
}