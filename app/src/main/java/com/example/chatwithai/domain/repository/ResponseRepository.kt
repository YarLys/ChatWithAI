package com.example.chatwithai.domain.repository

import com.example.chatwithai.data.remote.dto.ResponseDto

interface ResponseRepository {
    suspend fun request(prompt: String): ResponseDto
}