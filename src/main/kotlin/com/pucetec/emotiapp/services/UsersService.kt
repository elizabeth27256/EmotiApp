package com.pucetec.emotiapp.services

import com.pucetec.emotiapp.exceptions.UserAlreadyExistsException
import com.pucetec.emotiapp.exceptions.UserNotFoundException
import com.pucetec.emotiapp.mappers.UsersMapper
import com.pucetec.emotiapp.models.request.LoginRequest
import com.pucetec.emotiapp.models.request.RegisterRequest
import com.pucetec.emotiapp.models.responses.UsersResponse
import com.pucetec.emotiapp.repositories.UsersRepository
import org.springframework.stereotype.Service

@Service
class UsersService(
    private val usersRepository: UsersRepository,
    private val usersMapper: UsersMapper
) {

    fun register(request: RegisterRequest): UsersResponse {

        if (usersRepository.existsByUsername(request.username)) {
            throw UserAlreadyExistsException(
                "Username '${request.username}' already exists"
            )
        }

        if (usersRepository.existsByEmail(request.email)) {
            throw UserAlreadyExistsException(
                "Email '${request.email}' already exists"
            )
        }

        val user = usersMapper.toEntity(request)
        val savedUser = usersRepository.save(user)

        return usersMapper.toResponse(savedUser)
    }

    // ✅ LOGIN VA AQUÍ, AL MISMO NIVEL
    fun login(request: LoginRequest): UsersResponse {

        val user = usersRepository.findByEmail(request.email)
            ?: throw UserNotFoundException("Email no registrado")

        if (user.password != request.password) {
            throw IllegalArgumentException("Contraseña incorrecta")
        }

        return usersMapper.toResponse(user)
    }

    fun findById(id: Long): UsersResponse {
        val user = usersRepository.findById(id)
            .orElseThrow {
                UserNotFoundException("User with id $id not found")
            }

        return usersMapper.toResponse(user)
    }

    fun findByUsername(username: String): UsersResponse {
        val user = usersRepository.findByUsername(username)
            ?: throw UserNotFoundException(
                "User with username '$username' not found"
            )

        return usersMapper.toResponse(user)
    }

    fun findAll(): List<UsersResponse> =
        usersRepository.findAll()
            .map { usersMapper.toResponse(it) }

    fun update(id: Long, request: RegisterRequest): UsersResponse {

        val user = usersRepository.findById(id)
            .orElseThrow {
                UserNotFoundException("User with id $id not found")
            }

        user.username = request.username
        user.email = request.email
        user.password = request.password

        val updatedUser = usersRepository.save(user)
        return usersMapper.toResponse(updatedUser)
    }

    fun delete(id: Long) {
        val user = usersRepository.findById(id)
            .orElseThrow {
                UserNotFoundException("User with id $id not found")
            }

        usersRepository.delete(user)
    }
}

