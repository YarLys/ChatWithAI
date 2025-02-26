package com.example.chatwithai.domain.repository

import com.example.chatwithai.domain.model.Rag
import kotlinx.coroutines.flow.Flow

interface RagRepository {

    fun getAllRags(): Flow<List<Rag>>

    suspend fun getRagById(id: Int): Rag?

    suspend fun insertRag(rag: Rag)

    suspend fun deleteRag(rag: Rag)
}