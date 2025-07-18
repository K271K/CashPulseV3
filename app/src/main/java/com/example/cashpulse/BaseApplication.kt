package com.example.cashpulse

import android.app.Application
import com.example.cashpulse.di.AppComponent
import com.example.cashpulse.di.DaggerAppComponent

class BaseApplication : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder()
            .application(this)
            .build()
    }
}