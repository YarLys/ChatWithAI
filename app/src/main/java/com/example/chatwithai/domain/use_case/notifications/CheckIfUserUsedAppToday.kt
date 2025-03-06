package com.example.chatwithai.domain.use_case.notifications

import com.example.chatwithai.domain.repository.UserPreferences
import java.util.Calendar
import javax.inject.Inject

class CheckIfUserUsedAppToday @Inject constructor(
    private val userPreferences: UserPreferences
) {
    operator fun invoke(): Boolean {
        val lastLoginDate = userPreferences.getLastLoginDate()
        val today = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis

        return lastLoginDate >= today + 36000   // notification in 10:00 if user didn't use app
    }
}