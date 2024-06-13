package com.example.todoapp.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Message
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.todoapp.R
import com.example.todoapp.view.MainActivity

//class NotificationHelper(val context:Context) {
//    private val CHANNEL_ID = "Todo_channel_id"
//    private val NOTOFICATION_ID = 1
//    companion object{
//        val REQUEST_NOTIF = 100
//    }
//
//    private fun createNotificationChannel(){
//        if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.O){
//            val channel = NotificationChannel(CHANNEL_ID,CHANNEL_ID,NotificationManager.IMPORTANCE_DEFAULT).apply {
//                description = "Todo Channel Description"
//            }
//            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE)
//                    as NotificationManager
//            notificationManager.createNotificationChannel(channel)
//
//        }
//    }
//
//    fun createNotification(title:String,message: String){
//        createNotificationChannel()
//        val intent = Intent(context,MainActivity::class.java).apply { flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK }
//        val pendingIntent = PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_IMMUTABLE)
//        val icon = BitmapFactory.decodeResource(context.resources, R.drawable.todochar)
//
//        val notification = NotificationCompat.Builder(context,CHANNEL_ID).setSmallIcon(R.drawable.checklist).setLargeIcon(icon).setContentTitle(title).setContentText(message).setStyle(NotificationCompat.BigPictureStyle().bigPicture(icon).bigLargeIcon(null)).setContentIntent(pendingIntent).setPriority(NotificationCompat.PRIORITY_DEFAULT).build()
//
//        try{
//            NotificationManagerCompat.from(context).notify(NOTOFICATION_ID,notification)
//        }catch (e:SecurityException){
//            Log.e("error",e.toString())
//        }
//    }
//}

