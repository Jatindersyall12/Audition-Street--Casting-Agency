package com.auditionstreet.castingagency.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import com.auditionstreet.castingagency.R
import com.auditionstreet.castingagency.storage.preference.Preferences
import com.auditionstreet.castingagency.ui.home.activity.HomeActivity
import com.auditionstreet.castingagency.utils.AppConstants
import com.google.firebase.messaging.FirebaseMessagingRegistrar
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FirebaseMessageReceiver : FirebaseMessagingService() {
    @Inject
    lateinit var preferences: Preferences

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d("From", "From: ${remoteMessage?.from}")
        // Check if message contains a notification payload.
        remoteMessage?.notification?.let {
            Log.d("Notification", "Message Notification Body: ${it.body}")
            //Message Services handle notification
           /* val notification = NotificationCompat.Builder(this)
                .setContentTitle(remoteMessage.from)
                .setContentText(it.body)
              //  .setSmallIcon(icon)
                .build()
            val manager = NotificationManagerCompat.from(applicationContext)
            manager.notify(*//*notification id*//*0, notification)*/
        }
    }
    override fun onNewToken(token: String) {
        //handle token
        Log.e("tokenn", "tokenn" + token)
        preferences.setString(AppConstants.FIREBASE_ID, token)
    }

}