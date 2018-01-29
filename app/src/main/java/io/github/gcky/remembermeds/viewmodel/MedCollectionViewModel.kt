package io.github.gcky.remembermeds.viewmodel

import android.arch.lifecycle.ViewModel
import io.github.gcky.remembermeds.data.MedRepository
import android.app.LauncherActivity.ListItem
import android.os.AsyncTask
import android.arch.lifecycle.LiveData
import io.github.gcky.remembermeds.data.Med


/**
 * Created by Gordon on 26-Jan-18.
 */

class MedCollectionViewModel(repository: MedRepository): ViewModel() {

    private var repository: MedRepository? = null

    init {
        this.repository = repository
    }

    fun getMeds(): LiveData<List<Med>> {
        return repository!!.getAllMeds()
    }

    fun getMedsNonLive(): List<Med> {
        return repository!!.getAllMedsNonLive()
    }

    fun deleteMed(med: Med) {
        val deleteMedTask = DeleteMedTask()
        deleteMedTask.execute(med)
    }

    private inner class DeleteMedTask: AsyncTask<Med, Void, Void>() {

        override fun doInBackground(vararg item: Med): Void? {
            repository!!.deleteMed(item[0])
            return null
        }
    }

}