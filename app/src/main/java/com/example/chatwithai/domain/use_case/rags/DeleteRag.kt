package com.example.chatwithai.domain.use_case.rags

import com.example.chatwithai.domain.model.Rag
import com.example.chatwithai.domain.repository.RagRepository
import javax.inject.Inject

class DeleteRag @Inject constructor(
    private val repository: RagRepository
) {
    suspend operator fun invoke(rag: Rag) {
        repository.deleteRag(rag)
    }
}