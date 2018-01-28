package io.github.gcky.remembermeds

import android.app.Activity
import android.app.Dialog
import android.app.TimePickerDialog
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AppCompatActivity
import android.text.format.DateFormat
import android.widget.Button
import android.widget.EditText
import android.widget.TimePicker
import io.github.gcky.remembermeds.data.Med
import io.github.gcky.remembermeds.viewmodel.MedCollectionViewModel
import io.github.gcky.remembermeds.viewmodel.NewMedViewModel
import javax.inject.Inject
import io.github.gcky.remembermeds.DetailActivity.TimePickerFragment
import android.text.format.DateFormat.is24HourFormat
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Context
import android.widget.TextView


/**
 * Created by Gordon on 26-Jan-18.
 */

class DetailActivity: FragmentActivity(), TimePickerDialog.OnTimeSetListener {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var newMedViewModel: NewMedViewModel
    private lateinit var reminderTimeInput: EditText
    private var reminderTimeHour = 0
    private var reminderTimeMinute = 0

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
        reminderTimeInput = findViewById(R.id.reminderTimeInput)

        saveBtn.setOnClickListener { _ ->
            val newMed = Med(medName=medNameInput.text.toString())
            newMedViewModel.addNewMedToDatabase(newMed)
            finish()
        }

        cancelBtn.setOnClickListener { _ -> finish() }

        reminderTimeInput.setOnClickListener { _ ->
            val newFragment = TimePickerFragment()
            newFragment.show(supportFragmentManager, "time_picker")
        }
    }

    override fun onTimeSet(p0: TimePicker?, p1: Int, p2: Int) {
        reminderTimeHour = p1
        reminderTimeMinute = p2
        reminderTimeInput.setText("$p1:$p2", TextView.BufferType.EDITABLE)
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