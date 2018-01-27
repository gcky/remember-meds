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
import javax.inject.Inject


/**
 * Created by Gordon on 23-Jan-18.
 */

class TodayFragment : Fragment() {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var medCollectionViewModel: MedCollectionViewModel
    private var meds: List<Med>? = null

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

        val observer: Observer<List<Med>> = Observer {
            fun onChanged(@Nullable meds: List<Med>) {
                if (this@TodayFragment.meds == null) {
                    this.meds = meds
//                    setListData(meds)
                }
            }
        }

        medCollectionViewModel.getMeds().observe(this, observer )
    }

    private class MyCustomAdapter(context: Context): BaseAdapter() {

        private val mContext: Context

        init {
            mContext = context
        }

        override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup?): View {
            val layoutInflater = LayoutInflater.from(mContext)
            val rowMain = layoutInflater.inflate(R.layout.row_main, viewGroup, false)
            return rowMain
//            val textView = TextView(mContext)
//            textView.text = "ROWWWW"
//            return textView
        }

        override fun getItem(position: Int): Any {
            return "TEST STRING"
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return 5
        }

    }
}