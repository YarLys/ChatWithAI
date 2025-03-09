package com.example.chatwithai.data

import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.Log
import com.example.chatwithai.common.Resource
import com.example.chatwithai.domain.util.FileWriter
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject

class FileWriterImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : FileWriter {

    override fun writeToFile(fileName: String, content: String, uri: Uri?): Resource<Unit> {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // Use Scoped Storage for Android 10+
                uri?.let {
                    context.contentResolver.openOutputStream(it)?.use { outputStream ->
                        outputStream.write(content.toByteArray())
                    }
                }
                Log.d("ExportData", "Wrote to file successfully")
                Resource.Success(Unit)
            } else {
                // for old Android versions use direct access to storage
                val documentsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                if (!documentsDir.exists()) {
                    documentsDir.mkdirs()
                }
                val file = File(documentsDir, fileName)
                file.writeText(content)
                Resource.Success(Unit)
            }
        } catch (e: Exception) {
            Log.d("ExportData", "ERROR: ${e.message}")
            Resource.Error(e.message ?: "Error in saving data")
        }
    }
}