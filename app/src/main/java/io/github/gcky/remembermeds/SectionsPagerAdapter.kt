package io.github.gcky.remembermeds

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

/**
 * Created by Gordon on 23-Jan-18.
 */

class SectionsPagerAdapter(fm: FragmentManager?) : FragmentPagerAdapter(fm) {

    var mFragmentList = ArrayList<Fragment>()
    var mFragmentTitleList = ArrayList<String>()

    override fun getItem(position: Int): Fragment {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        return mFragmentList.get(position)
    }

    override fun getCount(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        return mFragmentList.size
    }

    override fun getPageTitle(position: Int): CharSequence {
        return super.getPageTitle(position)
    }
}