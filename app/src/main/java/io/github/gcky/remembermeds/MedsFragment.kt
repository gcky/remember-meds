package io.github.gcky.remembermeds

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import io.github.gcky.remembermeds.data.Med
import io.github.gcky.remembermeds.viewmodel.MedCollectionViewModel
import javax.inject.Inject
import android.R.attr.button
import android.support.design.widget.Snackbar
import android.widget.Button


/**
 * Created by Gordon on 23-Jan-18.
 */

class MedsFragment : Fragment() {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var medCollectionViewModel: MedCollectionViewModel
    private var meds: List<Med>? = listOf(
            Med(0, "abcde", "New Med 1", "Breakfast", "Time"),
            Med(1, "axxxx", "New Med 2", "Dinner", "Time"),
            Med(2, "esdfe", "New Med 3", "Breakfast", "Time")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity.application as RememberMedApplication)
                .applicationComponent!!
                .inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.meds_fragment, container, false)

        val medsListView: ListView? = view?.findViewById(R.id.meds_list_view)
        medsListView?.adapter = MyCustomAdapter(activity)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        medCollectionViewModel = ViewModelProviders.of(activity, viewModelFactory)
                .get(MedCollectionViewModel::class.java)

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
        val medsListView: ListView? = view?.findViewById(R.id.meds_list_view)
        (medsListView?.adapter as BaseAdapter).notifyDataSetChanged()
    }

    private inner class MyCustomAdapter(context: Context): BaseAdapter() {

        private val mContext: Context

        init {
            mContext = context
        }

        override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup?): View {
            val layoutInflater = LayoutInflater.from(mContext)
            val rowMain = layoutInflater.inflate(R.layout.meds_row_main, viewGroup, false)
            val medNameTextView = rowMain.findViewById<TextView>(R.id.medsMedName)
            val medRoutineTextView = rowMain.findViewById<TextView>(R.id.medsRoutine)
            val medDeleteBtn = rowMain.findViewById<Button>(R.id.medsDeleteBtn)
            medNameTextView.text = meds!![position].medName
            medRoutineTextView.text = meds!![position].routine

            medDeleteBtn.setOnClickListener{ view ->
                medCollectionViewModel.deleteMed(meds!![position])
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