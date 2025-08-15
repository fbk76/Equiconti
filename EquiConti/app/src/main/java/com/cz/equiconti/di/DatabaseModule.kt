package com.cz.equiconti.di

import android.content.Context
import com.cz.equiconti.data.EquiDb
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
    fun provideDb(
        @ApplicationContext context: Context
    ): EquiDb = EquiDb.get(context)

    // (se ti serve anche il DAO direttamente)
    // @Provides fun provideOwnerDao(db: EquiDb) = db.ownerDao()
    // @Provides fun provideHorseDao(db: EquiDb) = db.horseDao()
    // @Provides fun provideTxnDao(db: EquiDb) = db.txnDao()
}
