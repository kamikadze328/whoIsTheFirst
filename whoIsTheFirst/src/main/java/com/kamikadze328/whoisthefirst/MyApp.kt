package com.kamikadze328.whoisthefirst

import android.app.Application
import com.kamikadze328.whoisthefirst.di.AppComponent
import com.kamikadze328.whoisthefirst.di.AppModule
import com.kamikadze328.whoisthefirst.di.DaggerAppComponent

class MyApp : Application() {
    lateinit var appComponent: AppComponent

    companion object {
        lateinit var instanse: MyApp
    }

    override fun onCreate() {
        super.onCreate()
        initComponent()
    }

    private fun initComponent() {
        appComponent = DaggerAppComponent.builder().appModule(AppModule(this)).build()
        instanse = this
        //appComponent.inject(this)

    }

}