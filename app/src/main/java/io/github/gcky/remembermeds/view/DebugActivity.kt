package io.github.gcky.remembermeds.view

import android.app.AlarmManager
import android.app.PendingIntent
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.arch.persistence.room.Room
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Paint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.ListView
import android.widget.TextView
import io.github.gcky.remembermeds.R
import io.github.gcky.remembermeds.RememberMedApplication
import io.github.gcky.remembermeds.data.Med
import io.github.gcky.remembermeds.data.MedDatabase
import io.github.gcky.remembermeds.receiver.ReminderReceiver
import io.github.gcky.remembermeds.util.Utils
import io.github.gcky.remembermeds.viewmodel.MedCollectionViewModel
import io.github.gcky.remembermeds.viewmodel.MedViewModel
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.inject.Inject

/**
 * Created by Gordon on 23-Jan-18.
 */

class DebugActivity : AppCompatActivity() {

    private var meds: List<Med>? = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.today_fragment)
        val database = Room.databaseBuilder(this, MedDatabase::class.java, "Med.db").build()
        val medDao = database.medDao()
        Single.fromCallable {
            medDao.getAllMedsNonLive()
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe { t ->
            println("RECEIVED MEDS FOR DEBUG")
            meds = t
            val todayListView: ListView? = this.findViewById(R.id.today_list_view)
            todayListView?.adapter = MyCustomAdapter(this)
            (todayListView?.adapter as BaseAdapter).notifyDataSetChanged()
        }
    }

    private inner class MyCustomAdapter(context: Context): BaseAdapter() {

        private val mContext: Context = context

        override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup?): View {
            val layoutInflater = LayoutInflater.from(mContext)
            val rowMain = layoutInflater.inflate(R.layout.debug_row_main, viewGroup, false)
            val medNameTextView = rowMain.findViewById<TextView>(R.id.debugMedName)
            val medRoutineTextView = rowMain.findViewById<TextView>(R.id.debugMedsRoutine)
            val med = meds!![position]
            medNameTextView.text = med.medName
            medRoutineTextView.text = "${med.routine} (${Utils().timeToString(med.reminderTimeHour, med.reminderTimeMinute)}) / Reminder ${if (med.reminderOn) "ON" else "OFF"} / On-time Count: ${med.onTimeCount}"
            return rowMain
        }

        override fun getItem(position: Int): Any {
            return meds!![position].medName
        }

        override fun getItemId(position: Int): Long {
            return meds!![position].uid
        }

        override fun getCount(): Int {
            return meds!!.size
        }

    }
}