package com.pichaelj.listshuffle

import android.app.Application
import timber.log.Timber

class ListShuffleApp : Application() {

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())
    }
}