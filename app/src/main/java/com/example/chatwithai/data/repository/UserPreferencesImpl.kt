package com.example.chatwithai.data.repository

import android.content.Context
import com.example.chatwithai.domain.repository.UserPreferences
import javax.inject.Inject

class UserPreferencesImpl @Inject constructor(
    private val context: Context
) : UserPreferences {

    private val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    override fun getLastLoginDate(): Long {
        return sharedPreferences.getLong(LAST_LOGIN_DATE_KEY, 0)
    }

    override fun setLastLoginDate(date: Long) {
        sharedPreferences.edit().putLong(LAST_LOGIN_DATE_KEY, date).apply()
    }

    companion object {
        private const val LAST_LOGIN_DATE_KEY = "last_login_date"
    }
}