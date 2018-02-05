package io.github.gcky.remembermeds.service

import android.annotation.TargetApi
import android.app.job.JobParameters
import android.app.job.JobService
import android.arch.persistence.room.Room
import android.content.Intent
import io.github.gcky.remembermeds.data.Med
import io.github.gcky.remembermeds.data.MedDao
import io.github.gcky.remembermeds.data.MedDatabase
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

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
        Single.fromCallable {
            medDao.updateMed(med)
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe { _ ->
            val intent = Intent()
            intent.action = "io.github.gcky.remembermeds.UPDATE_LIST_VIEW"
            sendBroadcast(intent)
        }

    }
}