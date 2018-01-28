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






/**
 * Created by Gordon on 28-Jan-18.
 */

class ReminderReceiver : BroadcastReceiver() {

    @TargetApi(26)
    override fun onReceive(context: Context, intent: Intent) {

        val bundle = intent.extras

        // The id of the channel.
        val CHANNEL_ID = "meds_reminder"
        val mChannel = NotificationChannel(CHANNEL_ID, "Medication reminders", NotificationManager.IMPORTANCE_DEFAULT)
        val mBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_add_black_24dp)
                .setContentTitle(bundle.get("medName").toString())
                .setContentText("Hello World!")
//        val resultIntent = Intent(context, MainActivity::class.java)
//
//// The stack builder object will contain an artificial back stack for the
//// started Activity.
//// This ensures that navigating backward from the Activity leads out of
//// your app to the Home screen.
//        val stackBuilder = TaskStackBuilder.create(context)
//// Adds the back stack for the Intent (but not the Intent itself)
//        stackBuilder.addParentStack(MainActivity::class.java)
//// Adds the Intent that starts the Activity to the top of the stack
//        stackBuilder.addNextIntent(resultIntent)
//        val resultPendingIntent = stackBuilder.getPendingIntent(
//                0,
//                PendingIntent.FLAG_UPDATE_CURRENT
//        )
//        mBuilder.setContentIntent(resultPendingIntent)
        val mNotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        mNotificationManager.createNotificationChannel(mChannel)

// mNotificationId is a unique integer your app uses to identify the
// notification. For example, to cancel the notification, you can pass its ID
// number to NotificationManager.cancel().
        println("NOTIFICATION SENT")
        mNotificationManager.notify(bundle.getLong("uid").toInt(), mBuilder.build())

    }
}