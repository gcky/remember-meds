package io.github.gcky.remembermeds.service

import android.annotation.TargetApi
import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.job.JobParameters
import android.app.job.JobService
import android.arch.persistence.room.Room
import android.content.Context
import android.content.Intent
import io.github.gcky.remembermeds.data.Med
import io.github.gcky.remembermeds.data.MedDao
import io.github.gcky.remembermeds.data.MedDatabase
import io.github.gcky.remembermeds.receiver.ReminderReceiver
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*

/**
 * Created by Gordon on 29-Jan-18.
 */

class MarkAsTakenJobService : JobService() {

    @TargetApi(21)
    override fun onStartJob(params: JobParameters): Boolean {
        println("MARK AS TAKEN SERVICE STARTED")
        val database = Room.databaseBuilder(this, MedDatabase::class.java, "Med.db").build()
        val medDao = database.medDao()
        Single.fromCallable {
            medDao.getMedByUidNonLive(params.extras.getLong("uid"))
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe { med ->
            println("RECEIVED MED")
            markAsTaken(med, medDao)
        }
        jobFinished(params, false)
        return true
    }

    override fun onStopJob(p0: JobParameters?): Boolean {
        return true
    }

    private fun markAsTaken(med: Med, medDao: MedDao) {
        println(med)
        println("Marking med as done")
        med.taken = true
        val calendar = Calendar.getInstance()
        val currentCalendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, med.reminderTimeHour)
        calendar.set(Calendar.MINUTE, med.reminderTimeMinute)
        calendar.add(Calendar.MINUTE, 30)
        if (currentCalendar.timeInMillis < calendar.timeInMillis) {
            med.onTimeCount += 1
            med.takenOnTime = true
            if (med.onTimeCount >= 7) med.reminderOn = false
        } else {
            med.takenOnTime = false
            med.onTimeCount = 0
            med.reminderOn = true
        }
        Single.fromCallable {
            medDao.updateMed(med)
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe { _ ->
            rescheduleAlarm(med)
            val intent = Intent()
            intent.action = "io.github.gcky.remembermeds.UPDATE_LIST_VIEW"
            sendBroadcast(intent)
        }

    }

    private fun rescheduleAlarm(med: Med) {
        if (med.reminderOn) {
            val calendar = Calendar.getInstance()
            val currentCalendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, med.reminderTimeHour)
            calendar.set(Calendar.MINUTE, med.reminderTimeMinute)
            if (med.taken) {
                calendar.add(Calendar.DATE, 1)
            } else {
                if (calendar.timeInMillis < currentCalendar.timeInMillis) {
                    calendar.add(Calendar.DATE, 1)
                }
            }
            val intentAlarm = Intent(this, ReminderReceiver::class.java)
            intentAlarm.putExtra("medName", med.medName)
            intentAlarm.putExtra("routine", med.routine)
            intentAlarm.putExtra("uid", med.uid)
            val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY, PendingIntent.getBroadcast(this, med.uid.toInt(), intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT))
        }
    }
}