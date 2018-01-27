package io.github.gcky.remembermeds.dependencyinjection

import android.app.Application
import dagger.Module
import dagger.Provides
import io.github.gcky.remembermeds.RememberMedApplication


/**
 * Created by Gordon on 26-Jan-18.
 */
@Module
class ApplicationModule(private val application: RememberMedApplication) {

    @Provides
    internal fun provideRoomDemoApplication(): RememberMedApplication {
        return application
    }

    @Provides
    internal fun provideApplication(): Application {
        return application
    }
}