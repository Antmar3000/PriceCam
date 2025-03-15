package com.antmar.pricecam

import android.app.Application
import com.antmar.pricecam.domain.utils.CrashLogger

class PriceCamApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Thread.setDefaultUncaughtExceptionHandler(CrashLogger(this))
    }
}