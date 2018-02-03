package io.github.gcky.remembermeds.receiver

import android.annotation.TargetApi
import android.app.NotificationManager
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import io.github.gcky.remembermeds.MarkAsTakenJobService
import android.os.PersistableBundle



/**
 * Created by Gordon on 28-Jan-18.
 */

class MarkAsTakenReceiver : BroadcastReceiver() {

    @TargetApi(23)
    override fun onReceive(context: Context, intent: Intent) {

        println("STARTING MARK AS TAKEN JOB SERVICE")
        val bundle = PersistableBundle()
        bundle.putLong("uid", intent.extras.getLong("uid"))

        val serviceComponent = ComponentName(context, MarkAsTakenJobService::class.java)
        val builder = JobInfo.Builder(0, serviceComponent)
        builder.setMinimumLatency((1 * 1000).toLong()) // wait at least
        builder.setOverrideDeadline((3 * 1000).toLong()) // maximum delay
        builder.setExtras(bundle)

        val jobScheduler = context.getSystemService(JobScheduler::class.java)
        jobScheduler.schedule(builder.build())

        val mNotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        mNotificationManager.cancel(bundle.getLong("uid").toInt())

    }

}