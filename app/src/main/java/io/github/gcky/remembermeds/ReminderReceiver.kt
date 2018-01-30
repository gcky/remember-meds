package io.github.gcky.remembermeds

import android.content.Intent
import android.content.BroadcastReceiver
import android.content.Context
import android.app.NotificationManager
import android.app.PendingIntent
import android.support.v4.app.NotificationCompat
import android.support.v4.app.TaskStackBuilder
import android.R.attr.name
import android.annotation.TargetApi
import android.app.NotificationChannel
import android.os.Bundle
import android.content.Intent.getIntent
import android.os.Build



/**
 * Created by Gordon on 28-Jan-18.
 */

class ReminderReceiver : BroadcastReceiver() {

    @TargetApi(26)
    override fun onReceive(context: Context, intent: Intent) {

        val bundle = intent.extras
        val CHANNEL_ID = "meds_reminder"

        val mBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_add_black_24dp)
                .setContentTitle(bundle.get("medName").toString())
                .setContentText("Take medication for routine: ${bundle.getString("routine")}")
        val mNotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val mChannel = NotificationChannel(CHANNEL_ID, "Medication reminders", NotificationManager.IMPORTANCE_HIGH)
            mNotificationManager.createNotificationChannel(mChannel)
        }

        println("NOTIFICATION SENT")
        mNotificationManager.notify(bundle.getLong("uid").toInt(), mBuilder.build())

    }
}