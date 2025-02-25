package com.example.chatwithai.presentation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.chatwithai.presentation.chat_screen.ChatScreen
import com.example.chatwithai.presentation.history.HistoryScreen
import com.example.chatwithai.presentation.rags.RagScreen


@Composable
fun NavHostContainer(
    navController: NavHostController,
    padding: PaddingValues
) {

    NavHost(
        navController = navController,
        startDestination = "chat", // set the start destination as chat
        modifier = Modifier.padding(paddingValues = padding), // Set the padding provided by scaffold

        builder = {

            // route : Chat
            composable("chat") {
                ChatScreen()
            }

            // route : rags
            composable("rags") {
                RagScreen()
            }

            // route : history
            composable("history") {
                HistoryScreen()
            }
        }
    )
}