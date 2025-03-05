package com.example.chatwithai.data.repository

import com.example.chatwithai.data.local.dao.RagDao
import com.example.chatwithai.domain.model.Rag
import com.example.chatwithai.domain.repository.RagRepository
import kotlinx.coroutines.flow.Flow

class RagRepositoryImpl(
    private val dao: RagDao
): RagRepository {
    override fun getAllRags(): Flow<List<Rag>> {
        return dao.getAllRags()
    }

    override suspend fun getRagById(id: Int): Rag? {
        return dao.getRagById(id)
    }

    override fun getStarredRags(): Flow<List<Rag>> {
        return dao.getStarredRags()
    }

    override suspend fun insertRag(rag: Rag) {
        return dao.insertRag(rag)
    }

    override suspend fun deleteRag(rag: Rag) {
        return dao.deleteRag(rag)
    }

    override suspend fun updateRag(rag: Rag) {
        dao.updateRag(rag)
    }
}