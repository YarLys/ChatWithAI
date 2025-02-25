package com.example.chatwithai.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Send
import com.example.chatwithai.presentation.BottomNavItem

object Constants {

    const val BASE_URL = "http://94.126.205.209:8000/"

    val BottomNavItems = listOf(
        BottomNavItem(
            label = "Чат",
            selectedIcon = Icons.Filled.Send,
            unselectedIcon = Icons.Outlined.Send,
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