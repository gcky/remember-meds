package io.github.gcky.remembermeds.data

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import android.arch.persistence.room.OnConflictStrategy.REPLACE

/**
 * Created by Gordon on 25-Jan-18.
 */
@Dao
interface MedDao {

    @Query("SELECT * FROM Med")
    fun getAllMeds(): LiveData<List<Med>>

    @Query("SELECT * FROM Med WHERE itemId = :arg0")
    fun getMedById(itemId: String): LiveData<Med>

    @Insert(onConflict = REPLACE)
    fun insert(med: Med)

    @Delete
    fun delete(med: Med)
}