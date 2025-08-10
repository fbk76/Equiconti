package com.cz.equiconti.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Owner::class, Horse::class, Txn::class],
    version = 2,                 // ⬅️ bump rispetto a prima
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
                .fallbackToDestructiveMigration() // ⬅️ wipe se schema cambia
                .build()
                    .also { INSTANCE = it }
            }
    }
}
