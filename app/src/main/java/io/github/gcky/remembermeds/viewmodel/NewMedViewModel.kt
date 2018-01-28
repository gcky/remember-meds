package io.github.gcky.remembermeds.viewmodel

import android.app.LauncherActivity.ListItem
import android.os.AsyncTask
import android.arch.lifecycle.ViewModel
import io.github.gcky.remembermeds.data.Med
import io.github.gcky.remembermeds.data.MedRepository


/**
 * Created by Gordon on 26-Jan-18.
 */

class NewMedViewModel internal constructor(private val repository: MedRepository) : ViewModel() {

    fun addNewMedToDatabaseNoAsyncTask(med: Med): Long {
        return repository.insertMed(med)
    }

    fun addNewMedToDatabase(med: Med) {
        AddMedTask().execute(med)
    }

    private inner class AddMedTask : AsyncTask<Med, Void, Void>() {

        override fun doInBackground(vararg med: Med): Void? {
            repository.insertMed(med[0])
            return null
        }
    }
}