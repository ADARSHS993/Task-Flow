package com.example.taskflow.presentation.notification

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class TaskReminderWorker(
    appContext: Context,
    workerParams: WorkerParameters
): CoroutineWorker(
    appContext,
    workerParams
){
    override suspend fun doWork(): Result {
        val taskId = inputData.getString("taskId")
            ?: return Result.failure()

        val taskTitle = inputData.getString("taskTitle")
            ?: return Result.failure()

        val reminderType = inputData.getString("reminderType")
            ?: "due"

        val message = when(reminderType){

            "24h" ->
                "Your task \"$taskTitle\" is duw tomorrow."

            "due" ->
                "Your task \"$taskTitle\" is due now."

            else ->
                "You have a task due soon: $taskTitle"
        }

        NotificationHelper.showTaskReminder(
            context = applicationContext,
            taskId = "${taskId}_$reminderType",
            title = "TaskFlow Reminder",
            message = message
        )

        return Result.success()
    }
}