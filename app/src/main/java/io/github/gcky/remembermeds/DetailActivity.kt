package io.github.gcky.remembermeds

import android.app.Activity
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import io.github.gcky.remembermeds.data.Med
import io.github.gcky.remembermeds.viewmodel.MedCollectionViewModel
import io.github.gcky.remembermeds.viewmodel.NewMedViewModel
import javax.inject.Inject

/**
 * Created by Gordon on 26-Jan-18.
 */

public class DetailActivity: AppCompatActivity() {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var newMedViewModel: NewMedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        (this.application as RememberMedApplication)
                .applicationComponent!!
                .inject(this)

        newMedViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(NewMedViewModel::class.java)

        val saveBtn = findViewById<Button>(R.id.detailSaveBtn)
        val medNameInput = findViewById<EditText>(R.id.medNameInput)

        saveBtn.setOnClickListener { view ->
            val newMed = Med(medName=medNameInput.text.toString())
            newMedViewModel.addNewMedToDatabase(newMed)
            finish()
        }
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == NEW_MED_EDIT_REQUEST) {
//            if (resultCode == Activity.RESULT_OK) {
//                TODO("FUCK")
//            }
//        }
//    }

}