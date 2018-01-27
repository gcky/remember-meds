package io.github.gcky.remembermeds

import android.arch.persistence.room.*
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import io.reactivex.Flowable
import android.app.LauncherActivity.ListItem
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.support.annotation.Nullable
import io.github.gcky.remembermeds.data.Med
import io.github.gcky.remembermeds.viewmodel.CustomViewModelFactory
import io.github.gcky.remembermeds.viewmodel.MedCollectionViewModel
import kotlinx.android.synthetic.main.today_fragment.*
import javax.inject.Inject


/**
 * Created by Gordon on 23-Jan-18.
 */

class TodayFragment : Fragment() {

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
        val view = inflater?.inflate(R.layout.today_fragment, container, false)

        val todayListView: ListView? = view?.findViewById(R.id.today_list_view)
        todayListView?.adapter = MyCustomAdapter(activity)

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
            val rowMain = layoutInflater.inflate(R.layout.row_main, viewGroup, false)
            val medNameTextView = rowMain.findViewById<TextView>(R.id.textView)
            val medRoutineTextView = rowMain.findViewById<TextView>(R.id.textView3)
            medNameTextView.text = meds!![position].medName
            medRoutineTextView.text = meds!![position].routine
            return rowMain
//            val textView = TextView(mContext)
//            textView.text = "ROWWWW"
//            return textView
        }

        override fun getItem(position: Int): Any {
//            return "TEST"
            return meds!![position].medName
        }

        override fun getItemId(position: Int): Long {
//            return 1
            return meds!![position].uid
        }

        override fun getCount(): Int {
//            return 5
            return meds!!.size
        }

    }
}