package com.example.chatwithai.domain.use_case.notifications

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.chatwithai.R
import javax.inject.Inject

class SendNotification @Inject constructor(
    private val notificationManager: NotificationManagerCompat
) {

    @SuppressLint("MissingPermission")
    operator fun invoke(context: Context, title: String, message: String) {
        val notificationId = 1
        val channelId = "daily_notification_channel"

        val channel = NotificationChannel(
            channelId,
            "Daily Notifications",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)   // maybe change later todo
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        notificationManager.notify(notificationId, notification)
    }
}