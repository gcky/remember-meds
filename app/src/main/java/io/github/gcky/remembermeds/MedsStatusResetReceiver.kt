package io.github.gcky.remembermeds

import android.annotation.TargetApi
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v4.app.NotificationCompat

/**
 * Created by Gordon on 28-Jan-18.
 */

class MedsStatusResetReceiver : BroadcastReceiver() {

    @TargetApi(23)
    override fun onReceive(context: Context, intent: Intent) {

        val serviceComponent = ComponentName(context, ResetMedsStatusJobService::class.java)
        val builder = JobInfo.Builder(1, serviceComponent)
        builder.setMinimumLatency((1 * 1000).toLong()) // wait at least
        builder.setOverrideDeadline((3 * 1000).toLong()) // maximum delay
        val jobScheduler = context.getSystemService(JobScheduler::class.java)
        jobScheduler.schedule(builder.build())

    }
}