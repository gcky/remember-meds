package io.github.gcky.remembermeds.viewmodel

import android.arch.lifecycle.ViewModel
import javax.inject.Inject
import android.arch.lifecycle.ViewModelProvider
import io.github.gcky.remembermeds.data.MedRepository
import javax.inject.Singleton



/**
 * Created by Gordon on 26-Jan-18.
 */

@Singleton
class CustomViewModelFactory @Inject
constructor(private val repository: MedRepository) : ViewModelProvider.Factory {

    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(MedCollectionViewModel::class.java))
            MedCollectionViewModel(repository) as T
        else if (modelClass.isAssignableFrom(MedViewModel::class.java))
            MedViewModel(repository) as T
        else if (modelClass.isAssignableFrom(NewMedViewModel::class.java))
            NewMedViewModel(repository) as T
        else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}