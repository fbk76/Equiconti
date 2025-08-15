package com.cz.equiconti.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface HorseDao {
    @Query("SELECT * FROM horse WHERE ownerId = :ownerId ORDER BY name")
    fun getHorses(ownerId: Long): Flow<List<Horse>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(horse: Horse)

    @Delete
    suspend fun delete(horse: Horse)
}
