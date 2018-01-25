package io.github.gcky.remembermeds.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

/**
 * Created by Gordon on 25-Jan-18.
 */
@Database(entities = arrayOf(Med::class), version = 1, exportSchema = false)
abstract class MedDatabase : RoomDatabase() {

    abstract fun medDao(): MedDao
}