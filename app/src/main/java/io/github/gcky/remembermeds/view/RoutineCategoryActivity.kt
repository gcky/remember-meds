package io.github.gcky.remembermeds.view

import android.app.Activity
import android.app.AlertDialog
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
import android.content.DialogInterface
import android.support.v7.widget.Toolbar
import android.text.InputType
import android.widget.EditText
import io.github.gcky.remembermeds.R


/**
 * Created by Gordon on 28-Jan-18.
 */

class RoutineCategoryActivity : AppCompatActivity() {

    private val categoryNames: ArrayList<String> = arrayListOf("Meals", "Home", "Sleep", "Custom")
    private val categoryDescriptions: ArrayList<String> = arrayListOf("Breakfast, Lunch, Dinner etc.", "Before leaving home, After arriving home etc.", "Before sleep, After wake-up etc.", "Specify your own routine")

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_routine_category)

        val myToolbar: Toolbar = findViewById<Toolbar>(R.id.routine_category_toolbar) as Toolbar
        setSupportActionBar(myToolbar)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.abc_ic_ab_back_material)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.title = "Routines"

        val listView = findViewById<ListView>(R.id.routine_category_list_view)
        listView.adapter = MyCustomAdapter(this)
        listView.setOnItemClickListener { adapterView, view, i, l ->
            if (categoryNames[i] == "Custom") {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Custom routine name")

                val input = EditText(this)
                input.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_NORMAL
                builder.setView(input)
                builder.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
                    val data = Intent()
                    data.putExtra("routine", input.text.toString())
                    setResult(RESULT_OK, data)
                    finish()
                })
                builder.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })

                builder.show()
            } else {
                val data = Intent(this, RoutineSelectionActivity::class.java)
                data.putExtra("category", categoryNames[i])
                startActivityForResult(data, 2)
            }
        }

    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {
                val finishData = Intent()
                finishData.putExtra("routine", data!!.extras.getString("routine"))
                setResult(RESULT_OK, finishData)
                finish()
            }
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
            routineCategoryName.text = categoryNames[position]
            routineCategoryDescription.text = categoryDescriptions[position]
            return rowMain
        }

        override fun getItem(position: Int): Any {
            return categoryNames[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return categoryNames.size
        }

    }

}