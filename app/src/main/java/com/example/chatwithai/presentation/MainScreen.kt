package com.example.chatwithai.presentation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { NavBottomMenu(navController) }
    ) { innerPadding ->
        NavHostContainer(navController = navController, padding = innerPadding)
    }
}