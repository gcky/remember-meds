package io.github.gcky.remembermeds

import android.annotation.TargetApi
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import java.util.*

/**
 * Created by Gordon on 28-Jan-18.
 */

class MedsStatusResetAlarmReceiver : BroadcastReceiver() {

    private val MEDS_STATUS_RESET_REQUEST_CODE = -1

    override fun onReceive(context: Context, intent: Intent) {

        println("SETTING RESET ALARM")
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        if (calendar.timeInMillis <= System.currentTimeMillis())
        {
            calendar.add(Calendar.DATE, 1)
        }
        val intentAlarm = Intent(context, MedsStatusResetReceiver::class.java)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY,
                PendingIntent.getBroadcast(context, MEDS_STATUS_RESET_REQUEST_CODE, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT))

    }
}