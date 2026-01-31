package com.pucetec.emotiapp.controllers

import com.pucetec.emotiapp.models.request.RegisterRequest
import com.pucetec.emotiapp.models.responses.UsersResponse
import com.pucetec.emotiapp.services.UsersService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
class UsersController(private val usersService: UsersService) {

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    fun register(@RequestBody request: RegisterRequest): UsersResponse {
        return usersService.register(request)
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): UsersResponse {
        return usersService.findById(id)
    }

    @GetMapping("/username/{username}")
    fun findByUsername(@PathVariable username: String): UsersResponse {
        return usersService.findByUsername(username)
    }

    @GetMapping
    fun findAll(): List<UsersResponse> {
        return usersService.findAll()
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody request: RegisterRequest): UsersResponse {
        return usersService.update(id, request)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: Long) {
        usersService.delete(id)
    }
}