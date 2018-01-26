package io.github.gcky.remembermeds

import android.arch.persistence.room.*
import android.content.Intent
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        mViewPager = findViewById(R.id.container)
        setupViewPager(mViewPager as ViewPager)

        val mTabLayout: TabLayout = findViewById(R.id.tabs)
        mTabLayout.setupWithViewPager(mViewPager)

//        database =  Room.databaseBuilder(this, MedDatabase::class.java, "we-need-db").build()

//        fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show()
//        }

        fab.setOnClickListener { view -> startDetailActivity() }

    }

    fun startDetailActivity() {
        val i: Intent = Intent(this, DetailActivity::class.java)
        startActivity(i)
    }

    fun setupViewPager(viewPager: ViewPager) {
        val adapter = SectionsPagerAdapter(supportFragmentManager)
        adapter.addFragment(TodayFragment(), "Today")
        adapter.addFragment(MedsFragment(), "Medications")
        viewPager.adapter = adapter
    }

}