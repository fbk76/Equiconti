package com.cz.equiconti

import android.app.Application
import com.cz.equiconti.util.CrashLogger

class EquiApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Nel caso in cui il provider non fosse stato caricato, assicuriamoci il logger
        CrashLogger.install(this)
        CrashLogger.writeBreadcrumb(this, "Application onCreate")
    }
}
