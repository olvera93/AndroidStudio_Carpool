package com.example.carpool.util.notifications

import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.carpool.R
import com.example.carpool.ui.register.RegisterActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class CarpoolMessaging : FirebaseMessagingService() {

    var notificationId = 0

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage.notification != null){
            sendNotification(remoteMessage.notification?.title, remoteMessage.notification?.body)
        }
    }

    private fun sendNotification(title: String?, body: String?) {
        Log.d("FCM_MESSAGE", "Cuerpo de la notificación: $body")

        val notificationLayout = RemoteViews(packageName, R.layout.notification_custom)
        val notificationlayoutExpanded = RemoteViews(packageName, R.layout.notification_custom_expanded)
        val notification = NotificationCompat.Builder(this, RegisterActivity.CHANNEL_FIREBASE)
            .setSmallIcon(R.drawable.ic_stat_name)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setCustomContentView(notificationLayout)
            .setCustomBigContentView(notificationlayoutExpanded)
            .build()

        NotificationManagerCompat.from(this).run {
            notify(++notificationId, notification)
        }

    }


}