package com.cz.equiconti.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        Owner::class,
        Horse::class,
        Txn::class           // <-- registrata anche Txn
    ],
    version = 1,
    exportSchema = false
)
abstract class EquiDb : RoomDatabase() {
    abstract fun ownerDao(): OwnerDao
    abstract fun horseDao(): HorseDao
    abstract fun txnDao(): TxnDao

    companion object {
        @Volatile private var INSTANCE: EquiDb? = null

        fun get(context: Context): EquiDb =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    EquiDb::class.java,
                    "equiconti.db"
                )
                // Se stai solo sviluppando e non ti interessa conservare dati
                // fra cambi di schema, abilita la distruzione automatica:
                .fallbackToDestructiveMigration()
                .build()
                .also { INSTANCE = it }
            }
    }
}
