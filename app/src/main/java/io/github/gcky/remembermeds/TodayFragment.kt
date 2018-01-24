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

/**
 * Created by Gordon on 23-Jan-18.
 */

class TodayFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.today_fragment, container, false)

        val todayListView: ListView? = view?.findViewById(R.id.today_list_view)
        todayListView?.adapter = MyCustomAdapter(activity)

        return view
    }

    private class MyCustomAdapter(context: Context): BaseAdapter() {

        private val mContext: Context

        init {
            mContext = context
        }

        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
            val textView = TextView(mContext)
            textView.text = "ROWWWW"
            return textView
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