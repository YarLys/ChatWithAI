package com.example.chatwithai.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Search
import com.example.chatwithai.presentation.navigation.BottomNavItem

object Constants {

    const val BASE_URL = "http://94.126.205.209:8000/"
    const val DATABASE_NAME = "rags_db"

    val BottomNavItems = listOf(
        BottomNavItem(
            label = "Чат",
            selectedIcon = Icons.AutoMirrored.Filled.Send,
            unselectedIcon = Icons.AutoMirrored.Outlined.Send,
            route = "chat"
        ),
        BottomNavItem(
            label = "RAG",
            selectedIcon = Icons.Filled.Edit,
            unselectedIcon = Icons.Outlined.Edit,
            route = "rags"
        ),
        BottomNavItem(
            label = "История",
            selectedIcon = Icons.Filled.Search,
            unselectedIcon = Icons.Outlined.Search,
            route = "history"
        )
    )
}