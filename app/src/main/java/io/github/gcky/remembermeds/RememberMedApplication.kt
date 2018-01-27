package io.github.gcky.remembermeds

import io.github.gcky.remembermeds.dependencyinjection.RoomModule
import io.github.gcky.remembermeds.dependencyinjection.ApplicationModule
import android.app.Application
import io.github.gcky.remembermeds.dependencyinjection.ApplicationComponent
import io.github.gcky.remembermeds.dependencyinjection.DaggerApplicationComponent


/**
 * Created by Gordon on 26-Jan-18.
 */

class RememberMedApplication : Application() {
    var applicationComponent: ApplicationComponent? = null

    override fun onCreate() {
        super.onCreate()

        applicationComponent = DaggerApplicationComponent
                .builder()
                .applicationModule(ApplicationModule(this))
                .roomModule(RoomModule(this))
                .build()

    }
}