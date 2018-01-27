package io.github.gcky.remembermeds.dependencyinjection

import android.app.Application
import dagger.Module
import io.github.gcky.remembermeds.data.MedDatabase
import android.arch.persistence.room.Room
import android.arch.lifecycle.ViewModelProvider
import javax.inject.Singleton
import dagger.Provides
import io.github.gcky.remembermeds.data.MedDao
import io.github.gcky.remembermeds.data.MedRepository
import io.github.gcky.remembermeds.viewmodel.CustomViewModelFactory


/**
 * Created by Gordon on 26-Jan-18.
 */

@Module
class RoomModule {

    private var database: MedDatabase? = null

    constructor(application: Application) {
        this.database = Room.databaseBuilder(
                application,
                MedDatabase::class.java,
                "Med.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideMedRepository(listItemDao: MedDao): MedRepository {
        return MedRepository(listItemDao)
    }

    @Provides
    @Singleton
    fun provideMedDao(database: MedDatabase): MedDao {
        return database.medDao()
    }

    @Provides
    @Singleton
    fun provideMedDatabase(application: Application): MedDatabase {
        return database!!
    }

    @Provides
    @Singleton
    fun provideViewModelFactory(repository: MedRepository): ViewModelProvider.Factory {
        return CustomViewModelFactory(repository)
    }

}