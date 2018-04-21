package io.github.gcky.remembermeds.view

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.BroadcastReceiver
import android.content.Intent
import android.graphics.Paint
import io.github.gcky.remembermeds.data.Med
import io.github.gcky.remembermeds.viewmodel.MedCollectionViewModel
import javax.inject.Inject
import io.github.gcky.remembermeds.viewmodel.MedViewModel
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import android.widget.*
import android.content.IntentFilter
import io.github.gcky.remembermeds.R
import io.github.gcky.remembermeds.RememberMedApplication
import io.github.gcky.remembermeds.receiver.ReminderReceiver
import io.github.gcky.remembermeds.util.Utils
import java.util.*


/**
 * Created by Gordon on 23-Jan-18.
 */

class TodayFragment : Fragment() {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var medCollectionViewModel: MedCollectionViewModel
    private lateinit var medViewModel: MedViewModel
    private var meds: List<Med>? = listOf()
    private val receiver = MyBroadcastReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity.application as RememberMedApplication)
                .applicationComponent!!
                .inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.today_fragment, container, false)

        val todayListView: ListView? = view?.findViewById(R.id.today_list_view)
        todayListView?.adapter = MyCustomAdapter(activity)
        (todayListView?.adapter as BaseAdapter).notifyDataSetChanged()

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        medCollectionViewModel = ViewModelProviders.of(activity, viewModelFactory)
                .get(MedCollectionViewModel::class.java)

        medViewModel = ViewModelProviders.of(activity, viewModelFactory)
                .get(MedViewModel::class.java)

        medCollectionViewModel.getMeds().observe(this,
                object: Observer<List<Med>> {
                    override fun onChanged(t: List<Med>?) {
                        println("MED LIST CHANGED")
                        setListData(t)
                    }

                })
    }

    fun setListData(medsList: List<Med>?) {
        println("INIT MEDS")
        println(medsList)
        meds = medsList?.sortedWith(compareBy(Med::reminderTimeHour, Med::reminderTimeMinute))
        val todayListView: ListView? = view?.findViewById(R.id.today_list_view)
        (todayListView?.adapter as BaseAdapter).notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()
        val intentFilter = IntentFilter()
        intentFilter.addAction("io.github.gcky.remembermeds.UPDATE_LIST_VIEW")
        context.registerReceiver(receiver, intentFilter)
        asyncUpdateList()
    }

    override fun onPause() {
        super.onPause()
        context.unregisterReceiver(receiver)
    }

    private inner class MyBroadcastReceiver: BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            asyncUpdateList()
        }
    }

    private fun asyncUpdateList() {
        println("UPDATE TODAY LIST")
        Single.fromCallable {
            medCollectionViewModel.getMedsNonLive()
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe { t ->
            println("UPDATE VIEW")
            setListData(t)
        }
    }

    private inner class MyCustomAdapter(context: Context): BaseAdapter() {

        private val mContext: Context = context

        override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup?): View {
            val layoutInflater = LayoutInflater.from(mContext)
            val rowMain = layoutInflater.inflate(R.layout.today_row_main, viewGroup, false)
            val medNameTextView = rowMain.findViewById<TextView>(R.id.medsMedName)
            val medRoutineTextView = rowMain.findViewById<TextView>(R.id.medsRoutine)
            val todayCheckBox = rowMain.findViewById<CheckBox>(R.id.todayCheckBox)
            val med = meds!![position]
            medNameTextView.text = med.medName
            medRoutineTextView.text = "${med.routine} (${Utils().timeToString(med.reminderTimeHour, med.reminderTimeMinute)})"

            if (med.taken) {
                medNameTextView.paintFlags = medNameTextView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                medRoutineTextView.paintFlags = medRoutineTextView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                todayCheckBox.isChecked = true
            } else {
                medNameTextView.paintFlags = medNameTextView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                medRoutineTextView.paintFlags = medRoutineTextView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                todayCheckBox.isChecked = false
            }
            
            todayCheckBox.setOnCheckedChangeListener { button, b ->
                med.taken = b
                val calendar = Calendar.getInstance()
                val currentCalendar = Calendar.getInstance()
                calendar.set(Calendar.HOUR_OF_DAY, med.reminderTimeHour)
                calendar.set(Calendar.MINUTE, med.reminderTimeMinute)
                calendar.add(Calendar.MINUTE, 30)
                if (b) {
                    if (currentCalendar.timeInMillis < calendar.timeInMillis) {
                        med.onTimeCount += 1
                        med.takenOnTime = true
                        if (med.onTimeCount >= 7) med.reminderOn = false
                    } else {
                        med.takenOnTime = false
                        med.onTimeCount = 0
                        med.reminderOn = true
                    }
                } else {
                    med.onTimeCount -= 1
                    if (med.takenOnTime && med.onTimeCount == 6) {
                        med.reminderOn = true
                    }
                }
                Single.fromCallable {
                    medViewModel.updateMed(med)
                }.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()).subscribe()
                rescheduleAlarm(med)
            }
            
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

        private fun rescheduleAlarm(med: Med) {
            if (med.reminderOn) {
                val calendar = Calendar.getInstance()
                val currentCalendar = Calendar.getInstance()
                calendar.set(Calendar.HOUR_OF_DAY, med.reminderTimeHour)
                calendar.set(Calendar.MINUTE, med.reminderTimeMinute)
                if (med.taken) {
                    calendar.add(Calendar.DATE, 1)
                } else {
                    if (calendar.timeInMillis < currentCalendar.timeInMillis) {
                        calendar.add(Calendar.DATE, 1)
                    }
                }
                val intentAlarm = Intent(context, ReminderReceiver::class.java)
                intentAlarm.putExtra("medName", med.medName)
                intentAlarm.putExtra("routine", med.routine)
                intentAlarm.putExtra("uid", med.uid)
                val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY, PendingIntent.getBroadcast(context, med.uid.toInt(), intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT))
            }
        }

    }
}