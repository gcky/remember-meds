package io.github.gcky.remembermeds.data

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import javax.inject.Inject

/**
 * Created by Gordon on 25-Jan-18.
 */
class MedRepository {

    var medDao: MedDao? = null

    @Inject
    constructor(medDao: MedDao) {
        this.medDao = medDao
    }

    fun getMed(itemId: String): LiveData<Med> {
        return medDao!!.getMedById(itemId)
    }

    fun getMedNonLive(uid: Long): Med {
        return medDao!!.getMedByUidNonLive(uid)
    }

    fun getAllMeds(): LiveData<List<Med>> {
        return medDao!!.getAllMeds()
    }

    fun getAllMedsNonLive(): List<Med> {
        return medDao!!.getAllMedsNonLive()
    }

    fun deleteMed(med: Med) {
        medDao!!.delete(med)
    }

    fun insertMed(med: Med): Long {
        return medDao!!.insert(med)
    }

    fun updateMed(med: Med) {
        medDao!!.updateMed(med)
    }
}