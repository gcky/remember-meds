package io.github.gcky.remembermeds

import android.annotation.TargetApi
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.app.job.JobScheduler
import android.app.job.JobInfo
import android.content.ComponentName




/**
 * Created by Gordon on 28-Jan-18.
 */

class RebootReceiver : BroadcastReceiver() {

    @TargetApi(26)
    override fun onReceive(context: Context, intent: Intent) {

        println("REBOOT RECEIVED FOR ALARMS")

//        val resetIntent = Intent(context, ResetAlarmsJobService::class.java)
//        context.startService(resetIntent)

        val serviceComponent = ComponentName(context, ResetAlarmsJobService::class.java)
        val builder = JobInfo.Builder(0, serviceComponent)
        builder.setMinimumLatency((1 * 1000).toLong()) // wait at least
        builder.setOverrideDeadline((3 * 1000).toLong()) // maximum delay
        val jobScheduler = context.getSystemService(JobScheduler::class.java)
        jobScheduler.schedule(builder.build())

//        val database = Room.inMemoryDatabaseBuilder(context, MedDatabase::class.java!!).build()
//        val medDao = database.medDao()
//        Single.fromCallable {
//            medDao.getAllMedsNonLive()
//        }.subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread()).subscribe { t ->
//            resetAlarms(context, t)
//        }

    }

//    private fun resetAlarms(context: Context, medList: List<Med>) {
//        for (med in medList) {
//            println("RESETTING ALARM")
//            val calendar = Calendar.getInstance()
//            calendar.set(Calendar.HOUR_OF_DAY, med.reminderTimeHour)
//            calendar.set(Calendar.MINUTE, med.reminderTimeMinute)
//            if (calendar.timeInMillis <= System.currentTimeMillis())
//            {
//                calendar.add(Calendar.DATE, 1)
//            }
//            val intentAlarm = Intent(context, ReminderReceiver::class.java)
//            intentAlarm.putExtra("medName", med.medName)
//            intentAlarm.putExtra("routine", med.routine)
//            intentAlarm.putExtra("uid", med.uid)
//            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY, PendingIntent.getBroadcast(context, med.uid.toInt(), intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT))
//        }
//    }
}