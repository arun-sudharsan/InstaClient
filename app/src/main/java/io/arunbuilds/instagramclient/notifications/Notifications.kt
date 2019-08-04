package io.arunbuilds.instagramclient.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import io.arunbuilds.instagramclient.R

object Notifications {

    private const val ID = "io.arunbuilds.instabioscraper"
    fun displayNotification(context: Context, title: String, task: String) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                ID,
                ID,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notification =
            NotificationCompat.Builder(context, ID)
                .setContentTitle(title)
                .setContentText(task)
                .setSmallIcon(R.drawable.ic_launcher_foreground)

        notificationManager.notify(1, notification.build())
    }
}