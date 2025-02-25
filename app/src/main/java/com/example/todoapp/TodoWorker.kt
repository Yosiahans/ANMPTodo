package com.example.todoapp

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.todoapp.util.NotificationHelper

class TodoWorker(context: Context,params:WorkerParameters):Worker(context,params) {
    override fun doWork(): Result {
        NotificationHelper(applicationContext).createNotification(
            inputData.getString("Title").toString(),inputData.getString("message").toString()
        )
        return Result.success()
    }
}