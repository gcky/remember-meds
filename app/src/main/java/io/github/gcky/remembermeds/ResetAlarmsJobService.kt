package io.github.gcky.remembermeds

import android.annotation.TargetApi
import android.app.AlarmManager
import android.content.Intent
import android.app.IntentService
import android.app.PendingIntent
import android.app.job.JobParameters
import android.app.job.JobService
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModelProviders
import android.arch.persistence.room.Room
import android.content.Context
import io.github.gcky.remembermeds.data.Med
import io.github.gcky.remembermeds.data.MedDatabase
import io.github.gcky.remembermeds.viewmodel.MedCollectionViewModel
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*


/**
 * Created by Gordon on 29-Jan-18.
 */

class ResetAlarmsJobService : JobService() {

    @TargetApi(21)
    override fun onStartJob(params: JobParameters): Boolean {
        println("ALARMS RESET SERVICE STARTED")
        val database = Room.databaseBuilder(this, MedDatabase::class.java, "Med.db").build()
        val medDao = database.medDao()
        Single.fromCallable {
            medDao.getAllMedsNonLive()
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe { t ->
            println("RECEIVED MEDS")
            resetAlarms(t)
        }
        jobFinished(params, false)
        return true
    }

    override fun onStopJob(p0: JobParameters?): Boolean {
        return true
    }

    private fun resetAlarms(medList: List<Med>) {
        println(medList)
        for (med in medList) {
            println("RESETTING ALARM")
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, med.reminderTimeHour)
            calendar.set(Calendar.MINUTE, med.reminderTimeMinute)
            if (calendar.timeInMillis <= System.currentTimeMillis())
            {
                calendar.add(Calendar.DATE, 1)
            }
            val intentAlarm = Intent(this, ReminderReceiver::class.java)
            intentAlarm.putExtra("medName", med.medName)
            intentAlarm.putExtra("routine", med.routine)
            intentAlarm.putExtra("uid", med.uid)
            val alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY, PendingIntent.getBroadcast(this, med.uid.toInt(), intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT))
        }
    }
}