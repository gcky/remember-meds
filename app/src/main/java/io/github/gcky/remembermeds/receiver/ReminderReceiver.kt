package io.github.gcky.remembermeds.receiver

import android.content.Intent
import android.content.BroadcastReceiver
import android.content.Context
import android.app.NotificationManager
import android.app.PendingIntent
import android.support.v4.app.NotificationCompat
import android.support.v4.app.TaskStackBuilder
import android.annotation.TargetApi
import android.app.NotificationChannel
import android.os.Build
import io.github.gcky.remembermeds.view.MainActivity
import io.github.gcky.remembermeds.R


/**
 * Created by Gordon on 28-Jan-18.
 */

class ReminderReceiver : BroadcastReceiver() {

    @TargetApi(26)
    override fun onReceive(context: Context, intent: Intent) {

        val bundle = intent.extras
        val CHANNEL_ID = "meds_reminder"
        val resultIntent = Intent(context, MainActivity::class.java)
        val stackBuilder = TaskStackBuilder.create(context)
        stackBuilder.addNextIntent(resultIntent)
        val resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)

        val mBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_add_black_24dp)
                .setContentTitle(bundle.get("medName").toString())
                .setContentText("Take medication for routine: ${bundle.getString("routine")}")
                .setContentIntent(resultPendingIntent)

        val actionIntent = Intent(context, MarkAsTakenReceiver::class.java)
        actionIntent.putExtra("uid", bundle.getLong("uid"))
        val actionPendingIntent = PendingIntent.getBroadcast(context, 9, actionIntent, PendingIntent.FLAG_CANCEL_CURRENT)
        val actionBuilder = NotificationCompat.Action.Builder(R.drawable.ic_done_black_24dp, "Mark As Taken", actionPendingIntent)
        mBuilder.addAction(actionBuilder.build())

        val mNotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val mChannel = NotificationChannel(CHANNEL_ID, "Medication reminders", NotificationManager.IMPORTANCE_HIGH)
            mNotificationManager.createNotificationChannel(mChannel)
        }
        println("NOTIFICATION SENT")
        mNotificationManager.notify(bundle.getLong("uid").toInt(), mBuilder.build())

    }
}