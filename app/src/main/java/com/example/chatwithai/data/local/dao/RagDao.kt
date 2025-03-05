package com.example.chatwithai.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.chatwithai.domain.model.Rag
import kotlinx.coroutines.flow.Flow

@Dao
interface RagDao {
    @Query("SELECT * FROM rag")
    fun getAllRags(): Flow<List<Rag>>

    @Query("SELECT * FROM rag WHERE id = :id")
    suspend fun getRagById(id: Int): Rag?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRag(rag: Rag)

    @Delete
    suspend fun deleteRag(rag: Rag)

    @Update
    suspend fun updateRag(rag: Rag)

    @Query("SELECT * FROM rag WHERE isStarred = 1")
    fun getStarredRags(): Flow<List<Rag>>
}