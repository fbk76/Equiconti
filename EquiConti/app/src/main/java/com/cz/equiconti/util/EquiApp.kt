package com.cz.equiconti.util

import android.app.Application
import com.cz.equiconti.data.EquiDb

class EquiApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Inizializzazione del database
        EquiDb.get(this)
    }
}
