package com.cz.equiconti.di

import android.content.Context
import androidx.room.Room
import com.cz.equiconti.data.AppDatabase
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
    fun provideDatabase(@ApplicationContext ctx: Context): AppDatabase {
        return Room.databaseBuilder(ctx, AppDatabase::class.java, "equiconti.db")
            .fallbackToDestructiveMigration() // 🔥 evita crash per mismatch schema (resetta il DB)
            .build()
    }

    @Provides
    fun provideDao(db: AppDatabase) = db.dao() // adatta ai tuoi DAO (es. ownersDao, horsesDao, ecc.)
}
