package com.example.chatwithai.domain.use_case

import com.example.chatwithai.domain.model.InvalidRagException
import com.example.chatwithai.domain.model.Rag
import com.example.chatwithai.domain.repository.RagRepository
import javax.inject.Inject
import kotlin.jvm.Throws

class AddRag @Inject constructor(
    private val repository: RagRepository
) {
    @Throws(InvalidRagException::class)
    suspend operator fun invoke(rag: Rag) {
        if (rag.title.isBlank()) {
            throw InvalidRagException("Заголовок RAG не может быть пустым")
        }
        if (rag.content.isBlank()) {
            throw InvalidRagException("Содержимое RAG не может быть пустым")
        }
        repository.insertRag(rag)
    }
}