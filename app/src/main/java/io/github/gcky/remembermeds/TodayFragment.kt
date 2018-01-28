package io.github.gcky.remembermeds

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.graphics.Paint
import android.widget.CheckBox
import io.github.gcky.remembermeds.data.Med
import io.github.gcky.remembermeds.viewmodel.MedCollectionViewModel
import javax.inject.Inject
import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import io.github.gcky.remembermeds.viewmodel.MedViewModel
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


/**
 * Created by Gordon on 23-Jan-18.
 */

class TodayFragment : Fragment() {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var medCollectionViewModel: MedCollectionViewModel
    private lateinit var medViewModel: MedViewModel
    private var meds: List<Med>? = listOf()

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
//                        if (this@TodayFragment.meds == null) {
//                            setListData(t)
//                        }
                        println(t)
                        setListData(t)
                    }

                })
    }

    fun setListData(medsList: List<Med>?) {
        println("INIT MEDS")
        println(medsList)
        meds = medsList
        val todayListView: ListView? = view?.findViewById(R.id.today_list_view)
        (todayListView?.adapter as BaseAdapter).notifyDataSetChanged()
    }

    private inner class MyCustomAdapter(context: Context): BaseAdapter() {

        private val mContext: Context

        init {
            mContext = context
        }

        override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup?): View {
            val layoutInflater = LayoutInflater.from(mContext)
            val rowMain = layoutInflater.inflate(R.layout.today_row_main, viewGroup, false)
            val medNameTextView = rowMain.findViewById<TextView>(R.id.medsMedName)
            val medRoutineTextView = rowMain.findViewById<TextView>(R.id.medsRoutine)
            val todayCheckBox = rowMain.findViewById<CheckBox>(R.id.todayCheckBox)
            val med = meds!![position]
            medNameTextView.text = med.medName
            medRoutineTextView.text = med.routine

            if (med.taken) {
                medNameTextView.paintFlags = medNameTextView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                todayCheckBox.isChecked = true
            } else {
                medNameTextView.paintFlags = medNameTextView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                todayCheckBox.isChecked = false
            }
            
            todayCheckBox.setOnCheckedChangeListener { button, b ->
                med.taken = b
                Single.fromCallable {
                    medViewModel.updateMed(med)
                }.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()).subscribe()
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

    }
}