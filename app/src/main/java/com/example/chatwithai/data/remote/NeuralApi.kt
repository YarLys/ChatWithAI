package com.example.chatwithai.data.remote

import com.example.chatwithai.data.remote.dto.RequestDto
import com.example.chatwithai.data.remote.dto.ResponseDto
import retrofit2.http.Body
import retrofit2.http.POST

interface NeuralApi {
    @POST("/generate")
    suspend fun request(@Body prompt: RequestDto): ResponseDto
}