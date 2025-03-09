package com.example.chatwithai.domain.util

import android.net.Uri
import com.example.chatwithai.common.Resource

interface FileWriter {
    fun writeToFile(fileName: String, content: String, uri: Uri?): Resource<Unit>
}