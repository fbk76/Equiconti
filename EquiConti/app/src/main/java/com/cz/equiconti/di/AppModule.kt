package com.cz.equiconti.di

import com.cz.equiconti.data.EquiDb
import com.cz.equiconti.data.Repo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // ❗️NON forniamo più EquiDb qui (lo fa DatabaseModule)

    @Provides
    @Singleton
    fun provideRepo(db: EquiDb): Repo = Repo(db)
}
