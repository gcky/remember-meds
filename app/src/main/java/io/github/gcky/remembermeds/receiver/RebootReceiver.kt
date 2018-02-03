package io.github.gcky.remembermeds.receiver

import android.annotation.TargetApi
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.app.job.JobScheduler
import android.app.job.JobInfo
import android.content.ComponentName
import io.github.gcky.remembermeds.ResetAlarmsJobService


/**
 * Created by Gordon on 28-Jan-18.
 */

class RebootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        println("REBOOT RECEIVED FOR ALARMS")
        resetReminderAlarms(context)
        setMedsStatusResetAlarm(context)

    }

    @TargetApi(26)
    private fun resetReminderAlarms(context: Context) {
        val serviceComponent = ComponentName(context, ResetAlarmsJobService::class.java)
        val builder = JobInfo.Builder(0, serviceComponent)
        builder.setMinimumLatency((1 * 1000).toLong()) // wait at least
        builder.setOverrideDeadline((3 * 1000).toLong()) // maximum delay
        val jobScheduler = context.getSystemService(JobScheduler::class.java)
        jobScheduler.schedule(builder.build())
    }

    private fun setMedsStatusResetAlarm(context: Context) {
        val intent = Intent(context, MedsStatusResetAlarmReceiver::class.java)
        context.sendBroadcast(intent)
    }

}