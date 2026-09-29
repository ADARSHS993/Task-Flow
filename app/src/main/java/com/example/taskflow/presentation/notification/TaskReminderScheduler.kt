package com.example.taskflow.presentation.notification

import android.content.Context
import android.icu.text.CaseMap
import android.provider.ContactsContract
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

object TaskReminderScheduler {

    private fun workName(
        taskId: String,
        type: String
    ): String{
        return "task_reminder_${taskId}_$type"
    }

    fun scheduleTaskReminders(
        context: Context,
        taskId: String,
        taskTitle: String,
        dueDate: Long?
    ){
        if( dueDate == null) return

        cancelTaskReminders(
            context = context,
            taskId = taskId
        )

        val now = System.currentTimeMillis()

        // 24 hour Reminder

        val oneDayBefore = dueDate - 24 * 60 * 60 * 1000L

        if(oneDayBefore > now){

            scheduleReminder(
                context = context,
                taskId = taskId,
                taskTitle = taskTitle,
                reminderType = "24h",
                triggerTime = oneDayBefore
            )
        }

        // Due Date Reminder

        if (dueDate > now) {

            scheduleReminder(
                context = context,
                taskId = taskId,
                taskTitle = taskTitle,
                reminderType = "due",
                triggerTime = dueDate
            )
        }
    }

    private fun scheduleReminder(
        context: Context,
        taskId: String,
        taskTitle: String,
        reminderType: String,
        triggerTime: Long
    ) {

        val delay = triggerTime - System.currentTimeMillis()

        if(delay <= 0) return

        val data = Data.Builder()
            .putString("taskId", taskId)
            .putString("taskTitle", taskTitle)
            .putString("reminderType", reminderType)
            .build()

        val request =
            OneTimeWorkRequestBuilder<TaskReminderWorker>()
                .setInputData(data)
                .setInitialDelay(
                    delay,
                    TimeUnit.MILLISECONDS
                )
                .build()

        WorkManager
            .getInstance(context)
            .enqueueUniqueWork(
                workName(
                    taskId,
                    reminderType
                ),
                androidx.work.ExistingWorkPolicy.REPLACE,
                request
            )
    }

    fun cancelTaskReminders(
        context: Context,
        taskId: String
    ) {

        val workManager =
            WorkManager.getInstance(context)

        workManager.cancelUniqueWork(
            workName(taskId, "24h")
        )

        workManager.cancelUniqueWork(
            workName(taskId, "due")
        )
    }
}