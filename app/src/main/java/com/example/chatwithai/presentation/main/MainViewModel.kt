package com.example.chatwithai.presentation.main

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.chatwithai.domain.repository.UserPreferences
import com.example.chatwithai.domain.use_case.notifications.CheckIfUserUsedAppToday
import com.example.chatwithai.domain.use_case.notifications.SendNotification
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val checkIfUserUsedAppTodayUseCase: CheckIfUserUsedAppToday,
    private val sendNotificationUseCase: SendNotification,
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _state = MutableStateFlow(MainState())
    val state: StateFlow<MainState> = _state

    fun checkIfUserLoggedInToday() {
        val hasLoggedInToday = checkIfUserUsedAppTodayUseCase()
        _state.value = state.value.copy(
            showNotification = !hasLoggedInToday
        )

        // update the date of last login
        if (!hasLoggedInToday) {
            userPreferences.setLastLoginDate(System.currentTimeMillis())
        }
    }

    fun sendNotification(context: Context, title: String, message: String) {
        sendNotificationUseCase(context, title, message)
    }
}