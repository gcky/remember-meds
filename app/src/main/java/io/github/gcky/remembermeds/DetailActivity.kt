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
import android.content.Context
import android.support.v4.app.NotificationCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.*
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
        val medNameInput = findViewById<EditText>(R.id.medNameInput)
        val routineBtn = findViewById<Button>(R.id.routineBtn)
        reminderTimeInput = findViewById(R.id.reminderTimeInput)

        routineBtn.setOnClickListener { view ->
            val i = Intent(this, RoutineCategoryActivity::class.java)
            startActivityForResult(i, 1)
        }

        saveBtn.setOnClickListener { view ->
            val newMed = Med(medName = medNameInput.text.toString(), routine = routine)
//            newMedViewModel.addNewMedToDatabase(newMed)
            Single.fromCallable {
                newMedViewModel.addNewMedToDatabaseNoAsyncTask(newMed)
            }.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe { id ->
                scheduleAlarm(view, id)
            }
            finish()
        }

        cancelBtn.setOnClickListener { _ -> finish() }

        reminderTimeInput.setOnClickListener { _ ->
            val newFragment = TimePickerFragment()
            newFragment.show(supportFragmentManager, "time_picker")
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                routine = data!!.extras.getString("routine")
            }
        }
    }

    override fun onTimeSet(p0: TimePicker?, p1: Int, p2: Int) {
        reminderTimeHour = p1
        reminderTimeMinute = p2
        reminderTimeInput.setText("$p1:$p2", TextView.BufferType.EDITABLE)
    }

    fun scheduleAlarm(v: View, uid: Long) {
        val calendar = Calendar.getInstance()
        if (calendar.timeInMillis  <= System.currentTimeMillis())
        {
            calendar.add(Calendar.DATE, 1)
        }
        calendar.set(Calendar.HOUR_OF_DAY, reminderTimeHour)
        calendar.set(Calendar.MINUTE, reminderTimeMinute)
        val intentAlarm = Intent(this, ReminderReceiver::class.java)
        intentAlarm.putExtra("medName", medNameInput.text.toString())
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