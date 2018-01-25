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
        var reminderTime: String = ""
)