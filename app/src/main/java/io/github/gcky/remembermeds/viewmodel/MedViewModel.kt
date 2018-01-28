package io.github.gcky.remembermeds.viewmodel

import android.app.LauncherActivity.ListItem
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import io.github.gcky.remembermeds.data.Med
import io.github.gcky.remembermeds.data.MedRepository


/**
 * Created by Gordon on 26-Jan-18.
 */

class MedViewModel internal constructor(private val repository: MedRepository) : ViewModel() {

    fun getMedById(itemId: String): LiveData<Med> {
        return repository.getMed(itemId)
    }

    fun updateMed(med: Med) {
        return repository.updateMed(med)
    }

}