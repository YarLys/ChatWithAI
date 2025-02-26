package com.example.chatwithai.domain.use_case

import com.example.chatwithai.domain.model.Rag
import com.example.chatwithai.domain.repository.RagRepository
import javax.inject.Inject

class GetRag @Inject constructor(
    private val repository: RagRepository
) {
    suspend operator fun invoke(id: Int): Rag? {
        return repository.getRagById(id)
    }
}