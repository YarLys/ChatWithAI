package com.example.chatwithai.presentation.main

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.chatwithai.presentation.navigation.NavBottomMenu
import com.example.chatwithai.presentation.navigation.NavHostContainer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    activity: Activity
) {

    val viewModel: MainViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()
    val navController = rememberNavController()

    var hasNotificationPermission by remember {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            mutableStateOf(
                ContextCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            )
        }
        else mutableStateOf(true)
    }

    var hasWriteStoragePermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            hasNotificationPermission = isGranted
            if (isGranted) {
                viewModel.checkIfUserLoggedInToday()
            }
        }
    )

    val writeStoragePermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            hasWriteStoragePermission = isGranted
        }
    )

    var canRecord by remember {
        mutableStateOf(ContextCompat.checkSelfPermission(
            activity,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED)
    }
    val recordAudioLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            canRecord = isGranted
        }
    )

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
            && !hasNotificationPermission) {
            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
        viewModel.checkIfUserLoggedInToday()

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q && !hasWriteStoragePermission) {
            writeStoragePermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }

        if (!canRecord) {
            recordAudioLauncher.launch(Manifest.permission.RECORD_AUDIO)
        }
    }

    if (state.showNotification && hasNotificationPermission) {
        LaunchedEffect(Unit) {
            viewModel.sendNotification(
                activity,
                "Напоминание",
                "Обязательно зайдите и задайте вопрос AI!"
            )
        }
    }

    Scaffold(
        bottomBar = { NavBottomMenu(navController) }
    ) { innerPadding ->
        NavHostContainer(navController = navController, padding = innerPadding)
    }
}