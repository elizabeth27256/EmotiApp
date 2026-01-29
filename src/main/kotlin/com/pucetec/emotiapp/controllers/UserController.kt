package com.pucetec.emotiapp.controllers

import com.pucetec.emotiapp.models.request.UserRequest
import com.pucetec.emotiapp.models.responses.UserResponse
import com.pucetec.emotiapp.services.UserService
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/users")
class UserController(private val userService: UserService) {

    @PostMapping
    fun save(@RequestBody user: UserRequest): UserResponse {
        return userService.save(user)
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): UserResponse {
        return userService.findById(id)
    }

    @GetMapping
    fun findByUserIdentifier(
        @RequestParam userIdentifier: String,
        @RequestParam(required = false)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        afterDate: LocalDateTime?
    ): List<UserResponse> {
        return userService.findByUserIdentifier(userIdentifier, afterDate)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long) {
        userService.delete(id)
    }
}