package io.github.gcky.remembermeds

import android.arch.persistence.room.*
import android.support.design.widget.TabLayout
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import io.reactivex.Flowable

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.meds_fragment.view.*
import kotlinx.android.synthetic.main.today_fragment.view.*

class MainActivity : AppCompatActivity() {

    /**
     * The [android.support.v4.view.PagerAdapter] that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * [android.support.v4.app.FragmentStatePagerAdapter].
     */
    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null
    private var mViewPager: ViewPager? = null
    public var database: RoomDatabase? = null

//    @Entity
//    data class Med(
//            @PrimaryKey(autoGenerate = true)
//            var uid: Long = 0,
//            var medName: String = "",
//            var routine: String = "",
//            var reminderTime: String = ""
//    )
//
//    @Dao
//    interface MedDao {
//
//        @Query("SELECT * FROM med")
//        fun getAllMeds(): Flowable<List<Med>>
//
//        @Insert
//        fun insert(med: Med)
//    }
//
//    @Database(entities = arrayOf(Med::class), version = 1, exportSchema = false)
//    abstract class MedDatabase : RoomDatabase() {
//
//        abstract fun medDao(): MedDao
//    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        mViewPager = findViewById(R.id.container)
        setupViewPager(mViewPager as ViewPager)

        var mTabLayout: TabLayout = findViewById(R.id.tabs)
        mTabLayout.setupWithViewPager(mViewPager)

//        database =  Room.databaseBuilder(this, MedDatabase::class.java, "we-need-db").build()

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

    }

    fun setupViewPager(viewPager: ViewPager) {
        var adapter = SectionsPagerAdapter(supportFragmentManager)
        adapter.addFragment(TodayFragment(), "Today")
        adapter.addFragment(MedsFragment(), "Medications")
        viewPager.adapter = adapter
    }

}