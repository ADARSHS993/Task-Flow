package com.example.taskflow.presentation.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import com.example.taskflow.R
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.util.jar.Manifest

object NotificationHelper {

    const val CHANNEL_ID = "taskflow_task_reminders"
    const val CHANNEL_NAME = "Task Reminders"
    const val CHANNEL_DESCRIPTION = "Notifications for task due dates"

    fun createNotificationChannel(context: Context){

        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){

            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = CHANNEL_DESCRIPTION
            }

            val notificationManager = context.getSystemService(
                Context.NOTIFICATION_SERVICE
            ) as NotificationManager

            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showTaskReminder(
        context: Context,
        taskId: String,
        title: String,
        message: String
    ){
        if(
            android.os.Build.VERSION.SDK_INT >= 33 &&
            context.checkSelfPermission(
                android.Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ){
            return
        }

        val notification = NotificationCompat.Builder(
            context,
            CHANNEL_ID
        )
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(message)
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context)
            .notify(taskId.hashCode(),notification)

    }
}