package com.cz.equiconti.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HorseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(horse: Horse): Long

    @Query("SELECT * FROM horses WHERE ownerId = :ownerId ORDER BY name")
    fun forOwner(ownerId: Long): Flow<List<Horse>>
}
