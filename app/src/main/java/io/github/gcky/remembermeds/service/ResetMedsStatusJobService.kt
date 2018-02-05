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

class ResetMedsStatusJobService : JobService() {

    @TargetApi(21)
    override fun onStartJob(params: JobParameters): Boolean {
        println("STATUS RESET SERVICE STARTED")
        val database = Room.databaseBuilder(this, MedDatabase::class.java, "Med.db").build()
        val medDao = database.medDao()
        Single.fromCallable {
            medDao.getAllMedsNonLive()
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe { t ->
            println("RECEIVED MEDS")
            resetStatuses(t, medDao)
        }
        jobFinished(params, false)
        return true
    }

    override fun onStopJob(p0: JobParameters?): Boolean {
        return true
    }

    private fun resetStatuses(medList: List<Med>, medDao: MedDao) {
        println(medList)
        Single.fromCallable {
            for (med in medList) {
                println("RESETTING STATUS")
                med.taken = false
                medDao.updateMed(med)
            }
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe { _ ->
            val intent = Intent()
            intent.action = "io.github.gcky.remembermeds.UPDATE_LIST_VIEW"
            sendBroadcast(intent)
        }

    }
}