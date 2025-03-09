package com.example.chatwithai.domain.use_case

import android.net.Uri
import android.util.Log
import com.example.chatwithai.common.Resource
import com.example.chatwithai.domain.repository.MessageRepository
import com.example.chatwithai.domain.util.FileWriter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class WriteToFile @Inject constructor(
    private val fileWriter: FileWriter,
    private val messageRepository: MessageRepository
) {
    suspend operator fun invoke(uri: Uri?): Resource<Unit> {
        return try {
            // get data from db
            val messages = messageRepository.getAllMessages()

            // convert data to string format
            val data = messages.map { messageList ->
                buildString {
                    append("Messages:\n")
                    messageList.forEach { message ->
                        append("Message ID: ${message.id}\n")
                        append("Request: ${message.request}, Response: ${message.response}\n")
                    }
                }
            }.first()

            // write data to file
            fileWriter.writeToFile("chat_export.txt", data, uri)

            Resource.Success(Unit)

        } catch (e: Exception) {
            Log.d("ExportData", "${e.message}")
            Resource.Error(e.message ?: "Error in saving data")
        }
    }
}