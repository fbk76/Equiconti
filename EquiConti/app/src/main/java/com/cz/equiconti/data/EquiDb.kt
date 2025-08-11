package com.cz.equiconti.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [Owner::class, Horse::class, Txn::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class EquiDb : RoomDatabase() {
    abstract fun ownerDao(): OwnerDao
    abstract fun horseDao(): HorseDao
    abstract fun txnDao(): TxnDao

    companion object {
        fun get(context: Context): EquiDb =
            Room.databaseBuilder(context, EquiDb::class.java, "equi.db")
                .fallbackToDestructiveMigration()
                .build()
    }
}
