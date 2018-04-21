package io.github.gcky.remembermeds.view

import android.content.Intent
import android.support.design.widget.TabLayout
import android.support.v7.app.AppCompatActivity

import android.support.v4.view.ViewPager
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.Menu
import io.github.gcky.remembermeds.R
import io.github.gcky.remembermeds.util.SaveMode

import kotlinx.android.synthetic.main.activity_main.*
import io.github.gcky.remembermeds.receiver.MedsStatusResetAlarmReceiver
import android.view.MenuInflater
import android.view.MenuItem


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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val myToolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(myToolbar)

        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        mViewPager = findViewById(R.id.container)
        setupViewPager(mViewPager as ViewPager)

        val mTabLayout: TabLayout = findViewById(R.id.tabs)
        mTabLayout.setupWithViewPager(mViewPager)

        fab.setOnClickListener { view -> startDetailActivity() }

        broadcastSetMedsStatusResetAlarmIntent()
    }

    private fun broadcastSetMedsStatusResetAlarmIntent() {
        val intent = Intent(this, MedsStatusResetAlarmReceiver::class.java)
        sendBroadcast(intent)
    }


    private fun startDetailActivity() {
        val i = Intent(this, DetailActivity::class.java)
        i.putExtra("mode", SaveMode.New)
        startActivity(i)
    }


    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = SectionsPagerAdapter(supportFragmentManager)
        adapter.addFragment(TodayFragment(), "Today")
        adapter.addFragment(MedsFragment(), "Medications")
        viewPager.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_debug -> {
                val debugIntent = Intent(this, DebugActivity::class.java)
                startActivity(debugIntent)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

}