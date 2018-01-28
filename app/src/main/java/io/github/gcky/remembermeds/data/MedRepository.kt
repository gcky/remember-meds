package io.github.gcky.remembermeds.data

import android.arch.lifecycle.LiveData
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

    fun getAllMeds(): LiveData<List<Med>> {
        return medDao!!.getAllMeds()
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