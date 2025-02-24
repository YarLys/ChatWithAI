package com.example.chatwithai.data.remote.dto

import com.example.chatwithai.domain.model.Response

data class ResponseDto(
    val response: String
)

fun ResponseDto.toResponse(): Response {
    return Response(response = response)
}
