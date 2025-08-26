package com.ticketpos.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class TicketPOSApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        
        // Initialize Timber for logging
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        
        // Initialize other app-wide configurations
        initializeApp()
    }
    
    private fun initializeApp() {
        Timber.d("Initializing TicketPOS Application")
        
        // Initialize database
        // Initialize preferences
        // Initialize crash reporting
        // Initialize analytics
    }
}