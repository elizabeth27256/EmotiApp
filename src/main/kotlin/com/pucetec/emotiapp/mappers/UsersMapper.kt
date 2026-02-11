package com.pucetec.emotiapp.mappers

import com.pucetec.emotiapp.models.entities.Users
import com.pucetec.emotiapp.models.request.RegisterRequest
import com.pucetec.emotiapp.models.responses.UsersResponse
import org.springframework.stereotype.Component

@Component
class UsersMapper {

    // 🔹 Request → Entity
    fun toEntity(request: RegisterRequest): Users {
        return Users(
            username = request.username,
            email = request.email,
            password = request.password
        )
    }

    // 🔹 Entity → Response
    fun toResponse(user: Users): UsersResponse {
        return UsersResponse(
            id = user.id,
            username = user.username,
            email = user.email
        )
    }
}
