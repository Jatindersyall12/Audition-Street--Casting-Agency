package com.auditionstreet.castingagency

import android.app.Application
import com.facebook.FacebookSdk
import dagger.hilt.android.HiltAndroidApp

@Suppress("DEPRECATION")
@HiltAndroidApp
class AppCastingAgency : Application() {
    override fun onCreate() {
        super.onCreate()
        FacebookSdk.sdkInitialize(applicationContext)
    }
}






