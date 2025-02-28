package com.example.chatwithai.presentation.util

sealed class Screen(val route: String) {
    object ChatScreen: Screen("chat")
    object RagsScreen: Screen("rags")
    object AddEditRagScreen: Screen("add_edit_rag")
    object HistoryScreen: Screen("history")
}