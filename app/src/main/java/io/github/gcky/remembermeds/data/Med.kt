package io.github.gcky.remembermeds.data

import android.arch.persistence.room.*
import io.reactivex.Flowable

/**
 * Created by Gordon on 25-Jan-18.
 */

@Entity
data class Med(
    @PrimaryKey(autoGenerate = true)
    var uid: Long = 0,
    var itemId: String = "",
    var medName: String = "",
    var routine: String = "",
    var reminderTimeHour: Int = 0,
    var reminderTimeMinute: Int = 0,
    var taken: Boolean = false,
    var onTimeCount: Int = 0,
    var reminderOn: Boolean = true,
    var takenOnTime: Boolean = false
)