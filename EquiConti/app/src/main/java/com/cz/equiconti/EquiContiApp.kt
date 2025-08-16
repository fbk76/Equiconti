package com.cz.equiconti

import android.app.Application
import com.cz.equiconti.data.EquiDb
import com.cz.equiconti.data.Repo

/**
 * Application di app. Espone un Repo creato dalla Room DB.
 * Se in MainActivity fai (application as EquiContiApp).repo, ora compila.
 *
 * Se NON usi Repo, puoi togliere tutto e lasciare una Application vuota.
 */
class EquiContiApp : Application() {

    // Rendi disponibile il repository a tutta l'app
    lateinit var repo: Repo
        private set

    override fun onCreate() {
        super.onCreate()

        // Inizializza DB Room
        val db = EquiDb.get(this)

        // Adatta il costruttore a come è definito Repo nel tuo progetto
        // (qui assumo un costruttore semplice che prende i DAO)
        repo = Repo(
            ownerDao = db.ownerDao(),
            horseDao = db.horseDao(),
            txnDao   = db.txnDao()
        )
    }
}
