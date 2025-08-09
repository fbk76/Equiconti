package com.cz.equiconti.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Owner::class],   // aggiungi Horse::class, Txn::class quando li avrai
    version = 1,
    exportSchema = false
)
abstract class EquiDb : RoomDatabase() {

    abstract fun ownerDao(): OwnerDao

    companion object {
        @Volatile private var INSTANCE: EquiDb? = null

        fun get(context: Context): EquiDb =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    EquiDb::class.java,
                    "equiconti.db"
                )
                    // in sviluppo è comodo: se cambi schema senza migrazioni, ricrea il DB
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
    }
}
