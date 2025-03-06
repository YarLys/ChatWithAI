package com.example.chatwithai.domain.repository

interface UserPreferences {
    fun getLastLoginDate(): Long
    fun setLastLoginDate(date: Long)
}