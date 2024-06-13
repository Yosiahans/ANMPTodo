package com.example.todoapp.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.todoapp.R
import com.example.todoapp.model.TodoDatabase
import com.example.todoapp.view.MainActivity

val DB_NAME = "newtododb"
val MIGRATION_1_2 =object : Migration(2,3){
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE todo ADD COLUMN isDone INTEGER DEFAULT 0 not null")
    }
}
val MIGRATION_3_4 =object : Migration(3,4){
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE todo ADD COLUMN todo_date INTEGER DEFAULT 0 not null")
    }
}

fun buildDb(context: Context):TodoDatabase{
    val db = Room.databaseBuilder(context,TodoDatabase::class.java, DB_NAME).addMigrations(
        MIGRATION_1_2).build()
    return db
}

class NotificationHelper(val context:Context) {
    private val CHANNEL_ID = "Todo_channel_id"
    private val NOTOFICATION_ID = 1
    companion object{
        val REQUEST_NOTIF = 100
    }

    private fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(CHANNEL_ID,CHANNEL_ID,
                NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = "Todo Channel Description"
            }
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE)
                    as NotificationManager
            notificationManager.createNotificationChannel(channel)

        }
    }

    fun createNotification(title:String,message: String){
        createNotificationChannel()
        val intent = Intent(context,
            MainActivity::class.java).apply { flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK }
        val pendingIntent = PendingIntent.getActivity(context,0,intent, PendingIntent.FLAG_IMMUTABLE)
        val icon = BitmapFactory.decodeResource(context.resources, R.drawable.todochar)

        val notification = NotificationCompat.Builder(context,CHANNEL_ID).setSmallIcon(R.drawable.checklist).setLargeIcon(icon).setContentTitle(title).setContentText(message).setStyle(
            NotificationCompat.BigPictureStyle().bigPicture(icon).bigLargeIcon(null)).setContentIntent(pendingIntent).setPriority(
            NotificationCompat.PRIORITY_DEFAULT).build()

        try{
            NotificationManagerCompat.from(context).notify(NOTOFICATION_ID,notification)
        }catch (e:SecurityException){
            Log.e("error",e.toString())
        }
    }
}

class TodoWorker(context: Context, params: WorkerParameters): Worker(context,params) {
    override fun doWork(): Result {
        val title = inputData.getString("title").toString()
        val message = inputData.getString("message").toString()
        NotificationHelper(applicationContext).createNotification(title, message)
        return Result.success()
        }
}
