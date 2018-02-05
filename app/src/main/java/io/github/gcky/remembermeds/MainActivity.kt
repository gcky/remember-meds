package io.github.gcky.remembermeds

import android.content.Intent
import android.support.design.widget.TabLayout
import android.support.v7.app.AppCompatActivity

import android.support.v4.view.ViewPager
import android.os.Bundle

import kotlinx.android.synthetic.main.activity_main.*
import io.github.gcky.remembermeds.receiver.MedsStatusResetAlarmReceiver


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

//    @TargetApi(26)
//    fun resetAlarms() {
//        val serviceComponent = ComponentName(this, ResetAlarmsJobService::class.java)
//        val builder = JobInfo.Builder(0, serviceComponent)
//        builder.setMinimumLatency((1 * 1000).toLong()) // wait at least
//        builder.setOverrideDeadline((3 * 1000).toLong()) // maximum delay
//        val jobScheduler = getSystemService(JobScheduler::class.java)
//        jobScheduler.schedule(builder.build())
//    }

    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = SectionsPagerAdapter(supportFragmentManager)
        adapter.addFragment(TodayFragment(), "Today")
        adapter.addFragment(MedsFragment(), "Medications")
        viewPager.adapter = adapter
    }

}