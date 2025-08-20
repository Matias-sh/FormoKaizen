package com.cocido.formokaizen

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class FormoKaizenApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        
        // Initialize any global components here
        // Analytics, Crash reporting, etc.
    }
}