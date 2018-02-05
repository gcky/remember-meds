package io.github.gcky.remembermeds

import android.app.*
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.text.format.DateFormat
import android.support.v4.app.FragmentActivity
import io.github.gcky.remembermeds.data.Med
import io.github.gcky.remembermeds.viewmodel.NewMedViewModel
import javax.inject.Inject
import android.app.TimePickerDialog.OnTimeSetListener
import android.arch.lifecycle.Observer
import android.arch.persistence.room.Room
import android.content.Context
import android.support.constraint.ConstraintLayout
import android.view.View
import android.widget.*
import io.github.gcky.remembermeds.data.MedDatabase
import io.github.gcky.remembermeds.receiver.ReminderReceiver
import io.github.gcky.remembermeds.util.Utils
import io.github.gcky.remembermeds.viewmodel.MedCollectionViewModel
import io.github.gcky.remembermeds.viewmodel.MedViewModel
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_detail.*
import java.util.*


/**
 * Created by Gordon on 26-Jan-18.
 */

class DetailActivity: FragmentActivity(), TimePickerDialog.OnTimeSetListener {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var newMedViewModel: NewMedViewModel
    private lateinit var reminderTimeInput: EditText
    private lateinit var detailSelectedRoutineName: TextView
    private lateinit var medNameInput: EditText
    private var reminderTimeHour = 0
    private var reminderTimeMinute = 0
    private var routine = "Routine"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        (this.application as RememberMedApplication)
                .applicationComponent!!
                .inject(this)

        newMedViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(NewMedViewModel::class.java)

        val saveBtn = findViewById<Button>(R.id.detailSaveBtn)
        val cancelBtn = findViewById<Button>(R.id.detailCancelBtn)
        medNameInput = findViewById<EditText>(R.id.medNameInput)
        val routineBtn = findViewById<ConstraintLayout>(R.id.detailSelectedRoutineBtn)
        reminderTimeInput = findViewById(R.id.reminderTimeInput)
        detailSelectedRoutineName = findViewById(R.id.detailSelectedRoutineName)

        if (intent.extras.get("mode") as SaveMode == SaveMode.Edit) {
            val database = Room.databaseBuilder(this, MedDatabase::class.java, "Med.db").build()
            val medDao = database.medDao()
            val uid = intent.extras.getLong("uid")
            Single.fromCallable {
                medDao.getMedByUidNonLive(uid)
            }.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe { med ->
                medNameInput.setText(med.medName)
                routine = med.routine
                detailSelectedRoutineName.text = routine
                reminderTimeHour = med.reminderTimeHour
                reminderTimeMinute = med.reminderTimeMinute
                reminderTimeInput.setText(Utils().timeToString(reminderTimeHour, reminderTimeMinute))
            }
        }

        routineBtn.setOnClickListener { view ->
            val i = Intent(this, RoutineCategoryActivity::class.java)
            startActivityForResult(i, 1)
        }

        saveBtn.setOnClickListener { _ -> saveMed() }

        cancelBtn.setOnClickListener { _ -> finish() }

        reminderTimeInput.setOnClickListener { _ ->
            val newFragment = TimePickerFragment()
            newFragment.show(supportFragmentManager, "time_picker")
        }
    }

    private fun saveMed() {
        val mode = intent.extras.get("mode") as SaveMode
        when (mode) {
            SaveMode.New -> {
                val newMed = Med(medName = medNameInput.text.toString(), routine = routine, reminderTimeHour = reminderTimeHour, reminderTimeMinute = reminderTimeMinute)
                Single.fromCallable {
                    newMedViewModel.addNewMedToDatabaseNoAsyncTask(newMed)
                }.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()).subscribe { uid ->
                    scheduleAlarm(uid)
                }
            }
            SaveMode.Edit -> {
                val database = Room.databaseBuilder(this, MedDatabase::class.java, "Med.db").build()
                val medDao = database.medDao()
                val uid = intent.extras.getLong("uid")
                Single.fromCallable {
                    medDao.getMedByUidNonLive(uid)
                }.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()).subscribe { med ->
                    val modifiedMed = Med(uid = med.uid, medName = medNameInput.text.toString(), routine = routine, reminderTimeHour = reminderTimeHour, reminderTimeMinute = reminderTimeMinute)
                    val medViewModel = ViewModelProviders.of(this, viewModelFactory)
                            .get(MedViewModel::class.java)
                    Single.fromCallable {
                        medViewModel.updateMed(modifiedMed)
                    }.subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread()).subscribe { _ ->
                        unscheduleAlarm(uid)
                        scheduleAlarm(uid)
                    }
                }
            }
        }
        finish()
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                routine = data!!.extras.getString("routine")
                detailSelectedRoutineName.text = routine
            }
        }
    }

    override fun onTimeSet(p0: TimePicker?, p1: Int, p2: Int) {
        reminderTimeHour = p1
        reminderTimeMinute = p2
        reminderTimeInput.setText(Utils().timeToString(p1, p2), TextView.BufferType.EDITABLE)
    }

    fun unscheduleAlarm(uid: Long) {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val cancelIntent = Intent(this, ReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, uid.toInt(), cancelIntent, 0)
        alarmManager.cancel(pendingIntent)
    }

    fun scheduleAlarm(uid: Long) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, reminderTimeHour)
        calendar.set(Calendar.MINUTE, reminderTimeMinute)
        if (calendar.timeInMillis <= System.currentTimeMillis())
        {
            calendar.add(Calendar.DATE, 1)
        }
        val intentAlarm = Intent(this, ReminderReceiver::class.java)
        intentAlarm.putExtra("medName", medNameInput.text.toString())
        intentAlarm.putExtra("routine", routine)
        intentAlarm.putExtra("uid", uid)
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY, PendingIntent.getBroadcast(this, uid.toInt(), intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT))
        Toast.makeText(this, "Medication added at $reminderTimeHour:$reminderTimeMinute", Toast.LENGTH_LONG).show()
    }

    class TimePickerFragment: DialogFragment() {

        var mListener: OnTimeSetListener? = null

        override fun onAttach(context: Context?) {
            super.onAttach(context)
            mListener = context as OnTimeSetListener
        }

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            return TimePickerDialog(activity, mListener, 12, 0,
                    DateFormat.is24HourFormat(activity))
        }
    }

}