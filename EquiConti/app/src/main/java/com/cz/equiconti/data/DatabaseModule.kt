package com.cz.equiconti.data

import android.content.Context
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
    fun provideEquiDb(@ApplicationContext context: Context): EquiDb =
        EquiDb.get(context)

    @Provides
    fun provideOwnerDao(db: EquiDb): OwnerDao = db.ownerDao()
}
