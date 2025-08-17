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
    fun provideDb(@ApplicationContext context: Context): EquiDb =
        Room.databaseBuilder(
            context,
            EquiDb::class.java,
            "equi.db"
        )
            // opzionale: cancella e ricrea se cambi schema senza migrazioni
            // .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideOwnerDao(db: EquiDb): OwnerDao = db.ownerDao()

    @Provides
    fun provideHorseDao(db: EquiDb): HorseDao = db.horseDao()

    @Provides
    fun provideTxnDao(db: EquiDb): TxnDao = db.txnDao()
}
