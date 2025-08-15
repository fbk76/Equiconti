package com.cz.equiconti.di

import android.content.Context
import androidx.room.Room
import com.cz.equiconti.data.EquiDb
import com.cz.equiconti.data.OwnerDao
import com.cz.equiconti.data.HorseDao
import com.cz.equiconti.data.TxnDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext ctx: Context): EquiDb {
        return Room.databaseBuilder(ctx, EquiDb::class.java, "equiconti.db")
            .fallbackToDestructiveMigration() // opzionale ma utile in debug
            .build()
    }

    @Provides
    fun provideOwnerDao(db: EquiDb): OwnerDao = db.ownerDao()

    @Provides
    fun provideHorseDao(db: EquiDb): HorseDao = db.horseDao()

    @Provides
    fun provideTxnDao(db: EquiDb): TxnDao = db.txnDao()
}
