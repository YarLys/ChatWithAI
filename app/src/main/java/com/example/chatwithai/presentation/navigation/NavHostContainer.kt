package com.example.chatwithai.presentation.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.chatwithai.presentation.add_edit_rag.AddEditRagScreen
import com.example.chatwithai.presentation.chat.ChatScreen
import com.example.chatwithai.presentation.history.HistoryScreen
import com.example.chatwithai.presentation.rags.RagScreen
import com.example.chatwithai.presentation.util.Screen


@Composable
fun NavHostContainer(
    navController: NavHostController,
    padding: PaddingValues
) {

    NavHost(
        navController = navController,
        startDestination = Screen.ChatScreen.route, // set the start destination as chat
        modifier = Modifier.padding(paddingValues = padding), // Set the padding provided by scaffold

        builder = {

            // route: Chat
            composable(Screen.ChatScreen.route) {
                ChatScreen()
            }

            // route: rags
            composable(Screen.RagsScreen.route) {
                RagScreen(navController)
            }

            // route: edit rag
            composable(
                Screen.AddEditRagScreen.route + "?ragId={ragId}",   // we have to pass ragId
                arguments = listOf(
                    navArgument(   // define an Id argument
                        name = "ragId"
                    ) {
                        type = NavType.IntType
                        defaultValue = -1
                    }
                )
            ) {
                AddEditRagScreen(navController)
            }

            // route: history
            composable(Screen.HistoryScreen.route) {
                HistoryScreen()
            }
        }
    )
}