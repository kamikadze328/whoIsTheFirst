package com.kamikadze328.whoisthefirst.di

import android.app.Application
import android.content.Context
import com.kamikadze328.whoisthefirst.activities.MainActivity
import com.kamikadze328.whoisthefirst.activities.MultiTouchActivity
import com.kamikadze328.whoisthefirst.activities.StatisticsActivity
import com.kamikadze328.whoisthefirst.data.TouchEventMapper
import com.kamikadze328.whoisthefirst.repository.ResourceRepository
import com.kamikadze328.whoisthefirst.repository.SharedPreferencesRepository
import com.kamikadze328.whoisthefirst.repository.SharedPreferencesRepositoryImpl
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    //fun inject(app: MyApp)
    fun injectActivity(activity: MultiTouchActivity)
    fun injectActivity(activity: MainActivity)
    fun injectActivity(activity: StatisticsActivity)
}


@Module
class AppModule(private val application: Application) {
    @Provides
    @Singleton
    fun providesApplicationContext(): Context = application

    @Provides
    @Singleton
    fun providesSharedPrefsRepository(context: Context): SharedPreferencesRepository =
        SharedPreferencesRepositoryImpl(context)

    @Provides
    @Singleton
    fun provideTouchEventMapper(): TouchEventMapper = TouchEventMapper()

    @Provides
    @Singleton
    fun provideResourceRepository(context: Context): ResourceRepository =
        ResourceRepository(context)
}