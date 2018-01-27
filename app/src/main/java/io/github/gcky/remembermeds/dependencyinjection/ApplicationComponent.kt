package io.github.gcky.remembermeds.dependencyinjection

import android.app.Application
import dagger.Component
import io.github.gcky.remembermeds.DetailActivity
import io.github.gcky.remembermeds.MedsFragment
import io.github.gcky.remembermeds.TodayFragment
import javax.inject.Singleton



/**
 * Created by Gordon on 26-Jan-18.
 */

@Singleton
@Component(modules = arrayOf( ApplicationModule::class, RoomModule::class ) )
interface ApplicationComponent {

    fun inject(todayFragment: TodayFragment)
    fun inject(medsFragment: MedsFragment)
    fun inject(detailActivity: DetailActivity)
//    fun inject(createFragment: CreateFragment)
//    fun inject(detailFragment: DetailFragment)

    fun application(): Application

}