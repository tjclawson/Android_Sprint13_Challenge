package com.example.android_sprint13_challenge

import android.app.Application

class App: Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent
            .builder()
            .bindApplication(this)
            .build()
    }
}