package com.cz.equiconti

import android.app.Application
import com.cz.equiconti.data.EquiDb
import com.cz.equiconti.data.Repo

class EquiContiApp : Application() {

    lateinit var repo: Repo
        private set

    override fun onCreate() {
        super.onCreate()

        val db = EquiDb.get(this)

        // ✅ Usa argomenti POSIZIONALI, nell'ordine previsto da Repo
        // (OwnerDao, HorseDao, TxnDao) – senza nomi
        repo = Repo(
            db.ownerDao(),
            db.horseDao(),
            db.txnDao()
        )
    }
}
