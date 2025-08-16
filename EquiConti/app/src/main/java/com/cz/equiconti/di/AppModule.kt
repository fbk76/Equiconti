package com.cz.equiconti.di

import android.content.Context
import com.cz.equiconti.data.EquiDb
import com.cz.equiconti.data.Repo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides @Singleton
    fun provideDb(@ApplicationContext ctx: Context): EquiDb = EquiDb.get(ctx)

    @Provides @Singleton
    fun provideRepo(db: EquiDb): Repo = Repo(db)
}
