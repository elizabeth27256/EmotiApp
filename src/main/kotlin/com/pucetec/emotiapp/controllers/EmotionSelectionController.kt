package com.pucetec.emotiapp.controllers

import com.pucetec.emotiapp.models.request.EmotionSelectionRequest
import com.pucetec.emotiapp.models.responses.EmotionSelectionResponse
import com.pucetec.emotiapp.services.EmotionSelectionService
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@CrossOrigin(origins = ["http://localhost:8100"])
@RestController
@RequestMapping("/api/selections")
class EmotionSelectionController(private val emotionSelectionService: EmotionSelectionService) {

    @PostMapping("/user/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    fun save(
        @PathVariable userId: Long,
        @RequestBody request: EmotionSelectionRequest
    ): EmotionSelectionResponse {
        return emotionSelectionService.save(userId, request)
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): EmotionSelectionResponse {
        return emotionSelectionService.findById(id)
    }

    @GetMapping("/user/{userId}/history")
    fun getHistory(
        @PathVariable userId: Long,
        @RequestParam(required = false)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        afterDate: LocalDateTime?
    ): List<EmotionSelectionResponse> {
        return emotionSelectionService.findByUserId(userId, afterDate)
    }

    @GetMapping
    fun findAll(): List<EmotionSelectionResponse> {
        return emotionSelectionService.findAll()
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: Long) {
        emotionSelectionService.delete(id)
    }
}