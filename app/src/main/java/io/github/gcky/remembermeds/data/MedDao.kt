package io.github.gcky.remembermeds.data

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.persistence.room.*
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Update
import javax.inject.Singleton


/**
 * Created by Gordon on 25-Jan-18.
 */
@Dao
interface MedDao {

    @Query("SELECT * FROM Med")
    fun getAllMeds(): LiveData<List<Med>>

    @Query("SELECT * FROM Med")
    fun getAllMedsNonLive(): List<Med>

    @Query("SELECT * FROM Med WHERE itemId = :itemId")
    fun getMedById(itemId: String): LiveData<Med>

    @Query("SELECT * FROM Med WHERE uid = :uid")
    fun getMedByUidNonLive(uid: Long): Med

    @Insert(onConflict = REPLACE)
    fun insert(med: Med): Long

    @Delete
    fun delete(med: Med)

    @Update
    fun updateMed(med: Med): Int

//    @Query("UPDATE Tour SET endAddress = :end_address  WHERE id = :tid)
//    fun updateMedById(long tid, String end_address);
}