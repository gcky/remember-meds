package io.github.gcky.remembermeds

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView

/**
 * Created by Gordon on 28-Jan-18.
 */

class RoutineSelectionActivity : AppCompatActivity() {

    private val mealNames: ArrayList<String> = arrayListOf("Breakfast", "Lunch", "Dinner")
    private val mealDescriptions: ArrayList<String> = arrayListOf("Every morning", "Every day at mid-day", "Every evening")

    private val homeNames: ArrayList<String> = arrayListOf("Before leaving home", "After arriving home")
    private val homeDescriptions: ArrayList<String> = arrayListOf("Take medication before leaving home everyday", "Take medication after arriving home everyday")

    private val sleepNames: ArrayList<String> = arrayListOf("Before sleep", "After wake-up")
    private val sleepDescriptions: ArrayList<String> = arrayListOf("Every night", "Every morning")

    private var selectedNames: ArrayList<String> = mealNames
    private var selectedDescriptions: ArrayList<String> = mealDescriptions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_routine_selection)

        val bundle = intent.extras
        when (bundle.getString("category")) {
            "Meals" -> {
                selectedNames = mealNames
                selectedDescriptions = mealDescriptions
            }
            "Home" -> {
                selectedNames = homeNames
                selectedDescriptions = homeDescriptions
            }
            "Sleep" -> {
                selectedNames = sleepNames
                selectedDescriptions = sleepDescriptions
            }
        }

        val listView = findViewById<ListView>(R.id.routine_selection_list_view)
        listView.adapter = MyCustomAdapter(this)
        listView.setOnItemClickListener { adapterView, view, i, l ->
            val data = Intent()
            data.putExtra("routine", selectedNames[i])
            setResult(RESULT_OK, data)
            finish()
        }

        val cancelBtn = findViewById<Button>(R.id.routineSelectionCancelBtn)
        cancelBtn.setOnClickListener { view ->
            setResult(Activity.RESULT_CANCELED)
            finish()
        }

    }

    private inner class MyCustomAdapter(context: Context): BaseAdapter() {

        private val mContext: Context

        init {
            mContext = context
        }

        override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup?): View {
            val layoutInflater = LayoutInflater.from(mContext)
            val rowMain = layoutInflater.inflate(R.layout.routine_category_row_main, viewGroup, false)
            val routineCategoryName = rowMain.findViewById<TextView>(R.id.routineCategoryName)
            val routineCategoryDescription = rowMain.findViewById<TextView>(R.id.routineCategoryDescription)
            routineCategoryName.text = selectedNames[position]
            routineCategoryDescription.text = selectedDescriptions[position]
            return rowMain
        }

        override fun getItem(position: Int): Any {
            return selectedNames[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return selectedNames.size
        }

    }

}