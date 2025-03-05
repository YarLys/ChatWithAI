package com.example.chatwithai.domain.use_case.rags

import android.util.Log
import com.example.chatwithai.domain.model.Rag
import com.example.chatwithai.domain.repository.RagRepository
import javax.inject.Inject

class UpdateRag @Inject constructor(
    private val repository: RagRepository
) {
    suspend operator fun invoke(rag: Rag) {
        Log.d("RagRepository", "Update rag: ${rag.content}, isStarred: ${rag.isStarred}")
        repository.updateRag(rag)
    }
}