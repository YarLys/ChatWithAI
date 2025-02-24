package com.example.chatwithai.data.repository

import com.example.chatwithai.data.remote.NeuralApi
import com.example.chatwithai.data.remote.dto.RequestDto
import com.example.chatwithai.data.remote.dto.ResponseDto
import com.example.chatwithai.domain.repository.ResponseRepository
import javax.inject.Inject

class ResponseRepositoryImpl @Inject constructor(
    private val api: NeuralApi
): ResponseRepository {
    override suspend fun request(prompt: String): ResponseDto {
        return api.request(RequestDto(prompt))
    }
}